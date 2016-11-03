import com.rabbitmq.client.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.*;

/**
 * Created by Tifani on 10/28/2016.
 */
public class NandeNyaaServer {
    private Connection connection = null;
    private Channel channel;
    private QueueingConsumer consumer;

    public NandeNyaaServer() {
        // Prepare database
        DatabaseHelper.openConnection();

        // Create new connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.SERVER_ADDRESS);
        try {
            // Create a connection and channel
            connection = factory.newConnection();
            channel = connection.createChannel();

            // Create a queue on the server
            // Active
            // Non-durable (won't survive a server restart)
            // Exclusive (other channels cannot connect to the same queue)
            // Not auto-deleted once it’s no longer being used
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("x-dead-letter-exchange", "some.exchange.name");
            channel.queueDeclare(Constants.SERVER_QUEUERE_NAME, false, false, true, args);
            channel.exchangeDeclare(Constants.EXCHANGE_NAME, "direct");

            // Accept only one un-ack-ed message at a time
            channel.basicQos(1);

            // Create the consumer
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(Constants.SERVER_QUEUERE_NAME, false, consumer);

            System.out.println(" [x] Awaiting RPC requests");

            while (true) {
                JSONObject response = null;
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                AMQP.BasicProperties props = delivery.getProperties();
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(props.getCorrelationId())
                        .deliveryMode(2)
                        .build();

                try {
                    JSONObject request = (JSONObject) (new JSONParser())
                            .parse(new String(delivery.getBody(), "UTF-8"));

                    System.out.println(" [.] "
                            + String.valueOf(request.get(Constants.REQUEST_TYPE)).toUpperCase()
                            + " from "
                            + String.valueOf(request.get(Constants.USERNAME)));
                    response = processRequest(request);
                } catch (Exception e) {
                    System.out.println(" [.] " + e.toString());
                    response = ResponseBuilder.buildUnknownErrorMessage("Unknown error");

                }
                finally {
                    channel.basicPublish("", props.getReplyTo(), replyProps, response.toJSONString().getBytes("UTF-8"));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {

                }
            }
        }
    }

    public JSONObject processRequest(JSONObject request) {
        JSONObject response = null;
        JSONArray memberArrJson = null;
        ArrayList<String> members = null;
        String message = null;
        int groupId;

        String type = String.valueOf(request.get(Constants.REQUEST_TYPE));
        String username = String.valueOf(request.get(Constants.USERNAME));

        switch (type) {
            case Constants.REGISTER:
                response = DatabaseHelper.register(
                        username,
                        String.valueOf(request.get(Constants.PASSWORD)));
                break;
            case Constants.LOGIN:
                response = DatabaseHelper.login(
                        username,
                        String.valueOf(request.get(Constants.PASSWORD)));
                break;
            case Constants.ADD_FRIEND:
                String newFriend = String.valueOf(request.get(Constants.USER_TO_ADD));
                response = DatabaseHelper.addFriend(username, newFriend);
                forwardMessage(newFriend, request);
                break;
            case Constants.CREATE_GROUP:
                memberArrJson = (JSONArray) request.get(Constants.MEMBERS);
                members = new ArrayList<>();
                for(Object el : memberArrJson){
                    members.add(String.valueOf(el));
                }
                response = DatabaseHelper.createGroup(
                        username,
                        String.valueOf(request.get(Constants.GROUP_NAME)),
                        members);
                break;
            case Constants.ADD_GROUP_MEMBERS:
                groupId = Integer.parseInt(String.valueOf(request.get(Constants.GROUP_ID)));
                if (!DatabaseHelper.isAdmin(username, groupId)) {
                    System.out.println("    {db} add_group_members failed: " + username + " is not an admin");
                    response = ResponseBuilder.buildAddGroupMembersFailedMessage("You aren't authorized to add group member(s)");
                } else {
                    memberArrJson = (JSONArray) request.get(Constants.MEMBERS);
                    members = new ArrayList<>();
                    for(Object el : memberArrJson){
                        members.add(String.valueOf(el));
                    }
                    response = DatabaseHelper.addGroupMembers(groupId, members);
                }
                break;
            case Constants.REMOVE_GROUP_MEMBERS:
                groupId = Integer.parseInt(String.valueOf(request.get(Constants.GROUP_ID)));
                if (!DatabaseHelper.isAdmin(username, groupId)) {
                    System.out.println("    {db} remove_group_members failed: " + username + " is not an admin");
                    response =  ResponseBuilder.buildAddGroupMembersFailedMessage("You aren't authorized to delete group member(s)");
                } else {
                    memberArrJson = (JSONArray) request.get(Constants.MEMBERS);
                    members = new ArrayList<>();
                    for (Object el : memberArrJson) {
                        members.add(String.valueOf(el));
                    }
                    response = DatabaseHelper.removeGroupMembers(groupId, members);
                }
                break;
            case Constants.EXIT_GROUP:
                groupId = Integer.parseInt(String.valueOf(request.get(Constants.GROUP_ID)));
                members = new ArrayList<>(Arrays.asList(new String[] {username}));
                response = DatabaseHelper.removeGroupMembers(groupId, members);
                if (response.get(Constants.STATUS).equals(Constants.SUCCESS)) {
                    response = ResponseBuilder.buildExitGroupMembersSuccessMessage("You have successfuly leave group");
                } else {
                    response = ResponseBuilder.buildExitGroupMembersFailedMessage("Error occurred. Please try again.");
                }
                break;
            case Constants.PRIVATE_MESSAGE:
                String receiver = String.valueOf(request.get(Constants.RECEIVER));
                forwardMessage(receiver, request);
                System.out.println("    private_message: send to " + receiver);
                response = ResponseBuilder.buildDeliverPrivateMessageSuccessMessage("Private chat has been delivered");
                break;
            case Constants.GROUP_MESSAGE:
                groupId = Integer.parseInt(String.valueOf(request.get(Constants.GROUP_ID)));
                JSONArray receivers = (JSONArray) DatabaseHelper.getGroupMembers(groupId).get(Constants.GROUP_MEMBERS);
                for (Object rec : receivers) {
                    System.out.println("    group_message: send to " + rec);
                    forwardMessage((String) rec, request);
                }
                response = ResponseBuilder.buildDeliverGroupMessageSuccessMessage("Group chat has been delivered");
                break;
            case Constants.GET_FRIENDS:
                response = DatabaseHelper.getFriends(username);
                break;
            case Constants.GET_GROUPS:
                response = DatabaseHelper.getGroups(username);
                break;
            case Constants.GET_GROUP_MEMBERS:
                groupId = Integer.parseInt(String.valueOf(request.get(Constants.GROUP_ID)));
                response = DatabaseHelper.getGroupMembers(groupId);
                break;
            default:
        }

        return response;
    }

    public void forwardMessage(String receiver, JSONObject message) {
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .deliveryMode(2)
                .priority(1)
                .build();

        try {
            Map<String, Object> args = new HashMap<>();
            args.put("x-dead-letter-exchange", "some.exchange.name");
            channel.queueDeclare(receiver, false, false, true, args);
            channel.basicPublish("", receiver, props, message.toJSONString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] argv) {
        NandeNyaaServer server = new NandeNyaaServer();
//        DatabaseHelper.openConnection();
//        DatabaseHelper.login("kucing", "meong");
    }
}

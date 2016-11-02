import com.rabbitmq.client.*;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Tifani on 10/29/2016.
 */
public class NandeNyaaClient {
    private Connection connetion;
    private Channel channel;
    private QueueingConsumer consumer;
    private String requestQueueName = Constants.SERVER_QUEUERE_NAME;
    private String replyQueueName;

    public NandeNyaaClient() throws Exception {
        // Create new connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.SERVER_ADDRESS);
        connetion = factory.newConnection();
        channel = connetion.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public String call(String message) throws Exception {
        String response = null;
        String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        // Wait for response
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody(), "UTF-8");
                break;
            }
        }

        return response;
    }

    public void close() throws Exception {
        connetion.close();
    }

    public static void main(String[] argv) {
        NandeNyaaClient client = null;
        String response = null;

        try {
            client = new NandeNyaaClient();

//            System.out.println(" [x] Register username: kucing password: meong");
//            response = client.call(
//                    RequestBuilder.buildRegisterMessage("quinsy", "quinsy")
//                            .toJSONString());

//            response = client.call(
//                    RequestBuilder.buildLoginMessage("kucing", "meong")
//                            .toJSONString());

//            response = client.call(
//                    RequestBuilder.buildAddFriendMessage("kucing", "acel")
//                            .toJSONString());

//            ArrayList members = new ArrayList();
//            members.add("tifani");
//            members.add("acel");
//            response = client.call(
//                    RequestBuilder.buildCreateGroupMessage("kucing", "Snowball Nyaw", members)
//                            .toJSONString());

//            ArrayList members = new ArrayList();
//            members.add("snowball");
//            response = client.call(
//                    RequestBuilder.buildAddGroupMembersMessage("kucing", 5, members)
//                            .toJSONString());

//            ArrayList members = new ArrayList();
//            members.add("snowball");
//            response = client.call(
//                    RequestBuilder.buildRemoveGroupMembersMessage("kucing", 5, members)
//                            .toJSONString());

//            response = client.call(
//                    RequestBuilder.buildExitGroupMessage("kucing", 5)
//                            .toJSONString());

//            response = client.call(
//                    RequestBuilder.buildGetFriendsMessage("kucing")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);
//
//            response = client.call(
//                    RequestBuilder.buildGetFriendsMessage("tifani")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);
//
//            response = client.call(
//                    RequestBuilder.buildGetFriendsMessage("acel")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);
//
//
//            response = client.call(
//                    RequestBuilder.buildGetFriendsMessage("sdf")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);

//            response = client.call(
//                    RequestBuilder.buildGetGroupsMessage("tifani")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);
//
//            response = client.call(
//                    RequestBuilder.buildGetGroupsMessage("snowball")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);
//
//            response = client.call(
//                    RequestBuilder.buildGetGroupsMessage("sdf")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);

            response = client.call(
                    RequestBuilder.buildGetGroupMembersMessage(1)
                            .toJSONString());
            System.out.println(" [.] Got response " + response);

            response = client.call(
                    RequestBuilder.buildGetGroupMembersMessage(3)
                            .toJSONString());
            System.out.println(" [.] Got response " + response);

            response = client.call(
                    RequestBuilder.buildGetGroupMembersMessage(5)
                            .toJSONString());
            System.out.println(" [.] Got response " + response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception ignore) {

                }
            }
        }
    }

}

import com.rabbitmq.client.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Tifani on 10/29/2016.
 */
public class NandeNyaaClient {
    private Connection connetion;
    private Channel channel;
    private QueueingConsumer queueConsumer;
    private Consumer emitConsumer;

    private String REQUEST_QUEUE_NAME = Constants.SERVER_QUEUERE_NAME;
    private String RESPONSE_QUEUE_NAME;
    private String CLIENT_QUEUE_NAME;
    private String EXCHANGE_NAME = Constants.EXCHANGE_NAME;

    private String activeUser = null;

    public NandeNyaaClient() throws Exception {
        // Create new connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.SERVER_ADDRESS);
        connetion = factory.newConnection();
        channel = connetion.createChannel();

        RESPONSE_QUEUE_NAME = channel.queueDeclare().getQueue();
        CLIENT_QUEUE_NAME = channel.queueDeclare().getQueue();
        System.out.println("Response queue: " + RESPONSE_QUEUE_NAME);
        queueConsumer = new QueueingConsumer(channel);

        emitConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received message: '" + message + "'");
            }
        };

        channel.basicConsume(RESPONSE_QUEUE_NAME, true, queueConsumer);
        // channel.basicConsume(CLIENT_QUEUE_NAME, true, emitConsumer);
    }

    public String call(String message) throws Exception {
        String response = null;
        String corrId = UUID.randomUUID().toString();
        System.out.println("Request: " + message);

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(RESPONSE_QUEUE_NAME)
                .build();

        // Send to server without exchange
        channel.basicPublish("", REQUEST_QUEUE_NAME, props, message.getBytes("UTF-8"));

        // Wait for response
        while (true) {
            QueueingConsumer.Delivery delivery = queueConsumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [.] Got response " + response);
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

            client.loginSuccess("kucing");
            response = client.call(
                    RequestBuilder.buildGroupMessage("kucing", 1, "Mari bermain bersama kucing-kucing")
                            .toJSONString());
            client.loginSuccess("blossom");
            client.loginSuccess("gyabo");
            client.loginSuccess("nyanko");
            client.loginSuccess("snowball");
//            response = client.call(
//                    RequestBuilder.buildPrivateMessage("tifani", "kucing", "Iya kucing lucu banget!")
//                            .toJSONString());
            while(true);
//            client = new NandeNyaaClient();
//            response = client.call(
//                    RequestBuilder.buildPrivateMessage("kucing", "tifani", "Nyanko nyan nyan")
//                            .toJSONString());
//
//            client = new NandeNyaaClient();
//            response = client.call(
//                    RequestBuilder.buildPrivateMessage("kucing", "tifani", "Miaw miaw")
//                            .toJSONString());

//            client.loginSuccess("acel");
//            System.out.println("ini acel");
//            response = client.call(
//                    RequestBuilder.buildGroupMessage("acel", 2, "TES ACEL")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);

//            client.loginSuccess("kucing");
//            System.out.println("ini kucing");
//            response = client.call(
//                    RequestBuilder.buildGroupMessage("kucing", 10, "TES KUCING")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);

//            client.loginSuccess("tifani");
//            System.out.println("ini tifani");
//            response = client.call(
//                    RequestBuilder.buildGroupMessage("tifani", 2, "TES TIFANI")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);

//            response = client.call(
//                    RequestBuilder.buildRegisterMessage("blossom", "blossom")
//                            .toJSONString());
//            response = client.call(
//                    RequestBuilder.buildRegisterMessage("nyanko", "nyanko")
//                            .toJSONString());
//            response = client.call(
//                    RequestBuilder.buildRegisterMessage("gyabo", "gyabo")
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);
//cre
//            System.out.println(" [.] Got response " + response);


//            response = client.call(
//                    RequestBuilder.buildLoginMessage("kucing", "meong")
//                            .toJSONString());


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
//                    RequestBuilder.buildCreateGroupMessage("kucing", "Kawaiina Nyanko", members)
//                            .toJSONString());
//             System.out.println(" [.] Got response " + response);

//
//            ArrayList members = new ArrayList();
//            members.add("blossom");
//            members.add("nyanko");
//            members.add("gyabo");
//            response = client.call(
//                    RequestBuilder.buildAddGroupMembersMessage("acel", 2, members)
//                            .toJSONString());

//            ArrayList members = new ArrayList();
//            members.add("snowball");
//            response = client.call(
//                    RequestBuilder.buildRemoveGroupMembersMessage("kucing", 9, members)
//                            .toJSONString());

//            response = client.call(
//                    RequestBuilder.buildExitGroupMessage("kucing", 1)
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

//            response = client.call(
//                    RequestBuilder.buildGetGroupMembersMessage(1)
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);
//
//            response = client.call(
//                    RequestBuilder.buildGetGroupMembersMessage(3)
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);
//
//            response = client.call(
//                    RequestBuilder.buildGetGroupMembersMessage(5)
//                            .toJSONString());
//            System.out.println(" [.] Got response " + response);

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

    public void loginSuccess(String username) {
        activeUser = username;
        try {
            channel.queueBind(CLIENT_QUEUE_NAME, EXCHANGE_NAME, username);
            channel.queueDeclare(username, false, false, true, null);
            channel.basicConsume(username, true, emitConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logoutSuccess(String username) {
        activeUser = null;
        try {
            channel.queueUnbind(CLIENT_QUEUE_NAME, EXCHANGE_NAME, username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

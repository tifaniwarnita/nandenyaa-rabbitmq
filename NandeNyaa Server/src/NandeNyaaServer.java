import com.rabbitmq.client.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
            channel.queueDeclare(Constants.SERVER_QUEUERE_NAME, false, false, true, null);

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
                        .build();

                try {
                    JSONObject request = (JSONObject) (new JSONParser())
                            .parse(new String(delivery.getBody(), "UTF-8"));

                    System.out.println(" [.] Get message: " + request.toJSONString());
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
        String type = String.valueOf(request.get(Constants.REQUEST_TYPE));
        System.out.println(request.toJSONString());
        System.out.println("Type: " + type);
        // Register
        if(type.equals(Constants.REGISTER)) {
            response = DatabaseHelper.register(
                    String.valueOf(request.get(Constants.USERNAME)),
                    String.valueOf(request.get(Constants.PASSWORD)));
        }

        return response;
    }

    public static void main(String[] argv) {
        NandeNyaaServer server = new NandeNyaaServer();
    }
}

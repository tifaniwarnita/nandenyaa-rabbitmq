import com.rabbitmq.client.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Tifani on 10/28/2016.
 */
public class NandeNyaaServer {
    private static final String SERVER_ADDRESS = "localhost";
    private static final String SERVER_QUEUE_NAME = "rpc_nandenyaa";
    private Connection connection = null;
    private Channel channel;
    private QueueingConsumer consumer;

    public NandeNyaaServer() {
        // Create new connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(SERVER_ADDRESS);
        try {
            // Create a connection and channel
            connection = factory.newConnection();
            channel = connection.createChannel();

            // Create a queue on the server
            // Active
            // Non-durable (won't survive a server restart)
            // Exclusive (other channels cannot connect to the same queue)
            // Not auto-deleted once it’s no longer being used
            channel.queueDeclare(SERVER_QUEUE_NAME, false, false, true, null);

            // Accept only one un-ack-ed message at a time
            channel.basicQos(1);

            // Create the consumer
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(SERVER_QUEUE_NAME, false, consumer);

            System.out.println(" [x] Awaiting RPC requests");

            while (true) {
                String response = null;
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                AMQP.BasicProperties props = delivery.getProperties();
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(props.getCorrelationId())
                        .build();

                try {
                    JSONObject message = (JSONObject) (new JSONParser())
                            .parse(new String(delivery.getBody(), "UTF-8"));

                    System.out.println(" [.] Get message: " + message.toJSONString());
                    response = "" + "Nyaw";
                } catch (Exception e) {
                    System.out.println(" [.] " + e.toString());
                    response = "";

                }
                finally {
                    channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));
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

    public static void main(String[] argv) {
        NandeNyaaServer server = new NandeNyaaServer();
    }
}

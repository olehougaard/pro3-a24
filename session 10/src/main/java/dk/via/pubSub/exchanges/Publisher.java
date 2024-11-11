package dk.via.pubSub.exchanges;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Publisher implements Closeable {
    private final Connection connection;
    private final Channel channel;

    public Publisher(String host) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void publish(String message, String... topics) throws IOException {
        for (String topic : topics) {
            channel.exchangeDeclare(topic, "fanout");
            channel.basicPublish(topic, "", null, message.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }
}

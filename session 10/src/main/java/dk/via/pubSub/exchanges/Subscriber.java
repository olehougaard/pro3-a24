package dk.via.pubSub.exchanges;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Subscriber implements Closeable {

    private final Connection connection;
    private final Channel channel;

    public Subscriber(String host) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void subscribe(String topic, MessageConsumer consumer) throws IOException {
        String queueName = channel.queueDeclare().getQueue();
        channel.exchangeDeclare(topic, "fanout");
        channel.queueBind(queueName, topic, "");
        channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            consumer.notify(message);
        }, consumerTag -> {
        });
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }
}

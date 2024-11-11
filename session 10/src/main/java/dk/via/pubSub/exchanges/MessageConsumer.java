package dk.via.pubSub.exchanges;

public interface MessageConsumer {
    void notify(String message);
}

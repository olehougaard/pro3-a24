package dk.via.cars.grpc.wrapper;

import io.grpc.stub.StreamObserver;

import java.util.function.Consumer;

public class ConsumerWrapper<Request, Response> extends GrpcWrapper<Request, Response> {
    private final Response message;
    private final Consumer<Request> consumer;

    public ConsumerWrapper(Response message, Consumer<Request> consumer) {
        this.message = message;
        this.consumer = consumer;
    }

    @Override
    public void call(Request request, StreamObserver<Response> responseObserver) {
        consumer.accept(request);
        responseObserver.onNext(message);
        responseObserver.onCompleted();
    }
}

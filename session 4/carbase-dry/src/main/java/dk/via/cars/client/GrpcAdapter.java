package dk.via.cars.client;

import dk.via.carbase.CarServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.function.Consumer;
import java.util.function.Function;

public class GrpcAdapter {
    private final String host;
    private final int port;

    public GrpcAdapter(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private ManagedChannel channel() {
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
    }

    public void execute(Consumer<CarServiceGrpc.CarServiceBlockingStub> consumer) {
        ManagedChannel managedChannel = channel();
        try {
            CarServiceGrpc.CarServiceBlockingStub stub = CarServiceGrpc.newBlockingStub(managedChannel);
            consumer.accept(stub);
        } finally {
            managedChannel.shutdown();
        }
    }

    public<R> R apply(Function<CarServiceGrpc.CarServiceBlockingStub, R> consumer) {
        ManagedChannel managedChannel = channel();
        try {
            CarServiceGrpc.CarServiceBlockingStub stub = CarServiceGrpc.newBlockingStub(managedChannel);
            return consumer.apply(stub);
        } finally {
            managedChannel.shutdown();
        }
    }

}

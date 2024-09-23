package via.pro3.grpcspringbootexample;

import io.grpc.ServerBuilder;
import via.pro3.grpcspringbootexample.service.TextConverterImpl;

public class Server {
    public static void main(String[] args) throws Exception {
        TextConverterImpl converter = new TextConverterImpl();
        io.grpc.Server server = ServerBuilder
                .forPort(9090)
                .addService(converter)
                .build();
        server.start();
        System.out.println("Server running");

        server.awaitTermination();
    }
}

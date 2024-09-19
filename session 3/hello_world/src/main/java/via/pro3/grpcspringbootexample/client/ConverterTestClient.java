package via.pro3.grpcspringbootexample.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import via.pro3.grpcspringbootexample.grpc.RequestText;
import via.pro3.grpcspringbootexample.grpc.ResponseText;
import via.pro3.grpcspringbootexample.grpc.TextConverterGrpc;

import java.util.Scanner;

public class ConverterTestClient {
    public static void main(String[] args) { // Added on August 21, 2024 to ask for user input
        Scanner input = new Scanner(System.in);
        System.out.println("Please Enter Your message: ");
        String message = input.nextLine();
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext() .build();
        TextConverterGrpc.TextConverterBlockingStub textConverterStub = TextConverterGrpc.newBlockingStub(managedChannel);
        RequestText request = RequestText.newBuilder() .setInputText(message) .build();
        RequestText request2 = RequestText.newBuilder() .setInputText("the first character should be capitalized") .build();
        ResponseText response = textConverterStub.toUpper(request);
        System.out.println("***************************************************************");
        System.out.println("Received response: " + response.getOutputText());
        System.out.println("===============================================================");
        ResponseText response2 = textConverterStub.capitalizeFirstCharacter(request2);
        System.out.println("Received response: " + response2.getOutputText());
        managedChannel.shutdown();
    }
}

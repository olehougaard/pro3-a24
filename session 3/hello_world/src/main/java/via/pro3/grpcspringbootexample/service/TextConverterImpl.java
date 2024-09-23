package via.pro3.grpcspringbootexample.service;

import io.grpc.stub.StreamObserver;
import via.pro3.grpcspringbootexample.grpc.RequestText;
import via.pro3.grpcspringbootexample.grpc.ResponseText;
import via.pro3.grpcspringbootexample.grpc.TextConverterGrpc;

public class TextConverterImpl extends TextConverterGrpc.TextConverterImplBase {
    @Override
    public void toUpper(RequestText request, StreamObserver<ResponseText> responseObserver) {
        // super.toUpper(request, responseObserver);
        System.out.println("Received Request ??? => " + request.toString());
        ResponseText responseText = ResponseText.newBuilder()
                .setOutputText(request.getInputText().toUpperCase()).build();
        responseObserver.onNext(responseText);
        responseObserver.onCompleted();
    }

    @Override
    public void capitalizeFirstCharacter(RequestText request, StreamObserver<ResponseText> responseObserver) {
        // super.capitalizeFirstCharacter(request, responseObserver);
        System.out.println("Received Request ??? => " + request.toString());

        String res = Character.toUpperCase(request.getInputText().charAt(0))
                + request.getInputText().substring(1).toLowerCase();

        ResponseText responseText = ResponseText.newBuilder()
                .setOutputText(res).build();
        responseObserver.onNext(responseText);
        responseObserver.onCompleted();
    }
}

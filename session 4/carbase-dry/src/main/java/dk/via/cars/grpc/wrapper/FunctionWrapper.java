package dk.via.cars.grpc.wrapper;

import io.grpc.stub.StreamObserver;

import java.util.function.Function;

public class FunctionWrapper<Request, Response> extends GrpcWrapper<Request, Response> {
    private final Function<Request, Response> function;

    public FunctionWrapper(Function<Request, Response> function) {
        this.function = function;
    }

    @Override
    public void call(Request request, StreamObserver<Response> responseObserver) {
        responseObserver.onNext(function.apply(request));
        responseObserver.onCompleted();
    }
}

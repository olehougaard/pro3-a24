package dk.via.cars.grpc.wrapper;

import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ExceptionWrapper<Request, Response> extends GrpcWrapper<Request, Response> {
    private final Class<? extends RuntimeException> exceptionClass;
    private final int code;
    private final Function<RuntimeException, String> message;
    private final GrpcWrapper<Request, Response> inner;
    private final Optional<BiConsumer<String, RuntimeException>> exceptionHandler;

    private ExceptionWrapper(
            GrpcWrapper<Request, Response> inner,
            Class<? extends RuntimeException> exceptionClass,
            int code,
            Function<RuntimeException, String> message,
            Optional<BiConsumer<String, RuntimeException>> exceptionHandler) {
        this.exceptionClass = exceptionClass;
        this.code = code;
        this.message = message;
        this.inner = inner;
        this.exceptionHandler = exceptionHandler;
    }

    public ExceptionWrapper(GrpcWrapper<Request, Response> inner, Class<? extends RuntimeException> exceptionClass, int code, Function<RuntimeException, String> message, BiConsumer<String, RuntimeException> exceptionHandler) {
        this(inner, exceptionClass, code, message, Optional.of(exceptionHandler));
    }

    public ExceptionWrapper(GrpcWrapper<Request, Response> inner, Class<? extends RuntimeException> exceptionClass, int code, Function<RuntimeException, String> message) {
        this(inner, exceptionClass, code, message, Optional.empty());
    }

    @Override
    public void call(Request request, StreamObserver<Response> responseObserver) {
        try {
            inner.call(request, responseObserver);
        } catch (RuntimeException e) {
            if (exceptionClass.isInstance(e)) {
                Status error = Status.newBuilder().setCode(code).setMessage(message.apply(e)).build();
                responseObserver.onError(StatusProto.toStatusRuntimeException(error));
                exceptionHandler.ifPresent(handler -> handler.accept(message.apply(e), e));
            } else {
                throw e;
            }
        }
    }
}

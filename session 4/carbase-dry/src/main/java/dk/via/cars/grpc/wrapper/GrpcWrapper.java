package dk.via.cars.grpc.wrapper;

import io.grpc.stub.StreamObserver;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class GrpcWrapper<Request, Response> {
    public class ExceptionsBuilderImpl {
        private final Class<? extends RuntimeException>[] exceptionClasses;

        @SafeVarargs
        private ExceptionsBuilderImpl(Class<? extends RuntimeException> ...exceptionClasses) {
            this.exceptionClasses = exceptionClasses;
        }

        public GrpcWrapper<Request, Response> doCatch(int code, String message) {
            return doCatch(code, e->message);
        }

        public GrpcWrapper<Request, Response> doCatch(int code, Function<RuntimeException, String> message) {
            GrpcWrapper<Request, Response> wrapper = GrpcWrapper.this;
            for(Class<? extends RuntimeException> e : exceptionClasses) {
                wrapper = wrapper.onException(e, code, message);
            }
            return wrapper;
        }

        public GrpcWrapper<Request, Response> doCatch(int code, String message, BiConsumer<String, RuntimeException> exceptionHandler) {
            return doCatch(code, e->message, exceptionHandler);
        }

        public GrpcWrapper<Request, Response> doCatch(int code, Function<RuntimeException, String> message, BiConsumer<String, RuntimeException> exceptionHandler) {
            GrpcWrapper<Request, Response> wrapper = GrpcWrapper.this;
            for(Class<? extends RuntimeException> e : exceptionClasses) {
                wrapper = wrapper.onException(e, code, message, exceptionHandler);
            }
            return wrapper;
        }
    }

    public static <Request, Response> GrpcWrapper<Request, Response> forFunction(Function<Request, Response> function) {
        return new FunctionWrapper<>(function);
    }

    public static <Request, Response> GrpcWrapper<Request, Response> forConsumer(Response message, Consumer<Request> consumer) {
        return new ConsumerWrapper<>(message, consumer);
    }

    public GrpcWrapper<Request, Response> onException(Class<? extends RuntimeException> exceptionClass, int code, String message) {
        return new ExceptionWrapper<>(this, exceptionClass, code, e->message);
    }

    public GrpcWrapper<Request, Response> onException(Class<? extends RuntimeException> exceptionClass, int code, Function<RuntimeException, String> message) {
        return new ExceptionWrapper<>(this, exceptionClass, code, message);
    }

    public GrpcWrapper<Request, Response> onException(Class<? extends RuntimeException> exceptionClass, int code, String message, BiConsumer<String, RuntimeException> exceptionHandler) {
        return new ExceptionWrapper<>(this, exceptionClass, code, e->message, exceptionHandler);
    }

    public GrpcWrapper<Request, Response> onException(Class<? extends RuntimeException> exceptionClass, int code, Function<RuntimeException, String> message, BiConsumer<String, RuntimeException> exceptionHandler) {
        return new ExceptionWrapper<>(this, exceptionClass, code, message, exceptionHandler);
    }

    @SafeVarargs
    public final ExceptionsBuilderImpl onExceptions(Class<? extends RuntimeException>... exceptionClasses) {
        return new ExceptionsBuilderImpl(exceptionClasses);
    }

    public abstract void call(Request request, StreamObserver<Response> responseObserver);
}

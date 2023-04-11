package ru.starfarm.grpc.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.experimental.Delegate;
import lombok.val;
import ru.starfarm.grpc.authorization.Authenticator;
import ru.starfarm.grpc.authorization.BasicAuthorizationServerInterceptor;

import java.util.Collection;
import java.util.Collections;

@Value
public class GrpcServer {

    @Delegate
    Server handle;

    @SneakyThrows
    GrpcServer(int port, Collection<BindableService> services, Collection<ServerInterceptor> interceptors) {
        val builder = ServerBuilder.forPort(port);
        interceptors.forEach(builder::intercept);
        services.forEach(builder::addService);
        handle = builder.build();
        start();
    }

    public static GrpcServer bind(int port, Collection<BindableService> service, Collection<ServerInterceptor> interceptors) {
        return new GrpcServer(port, service, interceptors);
    }

    public static GrpcServer bind(int port, Collection<BindableService> service) {
        return bind(port, service, Collections.emptyList());
    }

    public static GrpcServer bind(int port, BindableService service, ServerInterceptor interceptor) {
        return bind(port, Collections.singleton(service), Collections.singleton(interceptor));
    }

    public static GrpcServer bind(int port, BindableService service) {
        return bind(port, Collections.singleton(service));
    }

    public static GrpcServer bindWithBaseAuthorization(int port, BindableService service, Authenticator authenticator) {
        return bind(port, service, new BasicAuthorizationServerInterceptor(authenticator));
    }

}

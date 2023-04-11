package ru.starfarm.grpc.spring.config;

import io.grpc.BindableService;
import io.grpc.ServerInterceptor;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.starfarm.grpc.authorization.Authenticator;
import ru.starfarm.grpc.authorization.BasicAuthorizationServerInterceptor;
import ru.starfarm.grpc.server.GrpcServer;
import ru.starfarm.grpc.spring.annotation.GrpcAuthenticator;
import ru.starfarm.grpc.spring.annotation.GrpcServerInterceptor;
import ru.starfarm.grpc.spring.annotation.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties("grpc.server")
public class GrpcServerConfig {

    private int port;

    private transient ApplicationContext context;

    public GrpcServerConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public List<BindableService> grpcServices() {
        return context.getBeansWithAnnotation(GrpcService.class)
                .values().stream()
                .filter(bean -> bean instanceof BindableService)
                .map(bean -> (BindableService) bean)
                .collect(Collectors.toList());
    }

    @Bean
    public List<ServerInterceptor> grpcServerInterceptors() {
        val interceptors = context.getBeansWithAnnotation(GrpcServerInterceptor.class)
                .values().stream()
                .filter(bean -> bean instanceof ServerInterceptor)
                .map(bean -> (ServerInterceptor) bean)
                .collect(Collectors.toList());
        if (grpcServerAuthenticator() != null)
            interceptors.add(new BasicAuthorizationServerInterceptor(grpcServerAuthenticator()));
        return interceptors;
    }

    @Bean
    public Authenticator grpcServerAuthenticator() {
        return context.getBeansWithAnnotation(GrpcAuthenticator.class)
                .values().stream()
                .filter(bean -> bean instanceof Authenticator)
                .map(bean -> (Authenticator) bean)
                .findFirst()
                .orElse(null);
    }

    @Bean
    public GrpcServer grpcServer() {
        return GrpcServer.bind(port, grpcServices(), grpcServerInterceptors());
    }

}

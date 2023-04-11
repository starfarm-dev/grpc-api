package ru.starfarm.grpc.spring.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.starfarm.grpc.authorization.credentials.BasicCredentials;

@Getter
@Configuration
@ConfigurationProperties("grpc.auth")
public class GrpcClientConfig {

    private String login, token;

    @Bean
    public BasicCredentials baseCredentials() {
        return BasicCredentials.of(login, token);
    }

}

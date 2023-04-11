package ru.starfarm.grpc.authorization;

import io.grpc.Context;
import io.grpc.Metadata;
import lombok.experimental.UtilityClass;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

@UtilityClass
public class Authorization {

    public final String TYPE = "Basic";

    public final Metadata.Key<String> HEADER_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER);
    public final Context.Key<String> CONTEXT_LOGIN_KEY = Context.key("login");

    public String getContextLogin() {
        return CONTEXT_LOGIN_KEY.get();
    }

}

package ru.starfarm.grpc.authorization;

import io.grpc.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import ru.starfarm.grpc.authorization.credentials.Credentials;
import ru.starfarm.grpc.authorization.exception.AuthorizationException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
@RequiredArgsConstructor
public class BasicAuthorizationServerInterceptor implements ServerInterceptor {

    protected final Authenticator authenticator;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        val value = metadata.get(Authorization.HEADER_KEY);
        var status = Status.OK;

        if (value == null)
            status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing");
        else if (!value.startsWith(Authorization.TYPE))
            status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type");
        else {
            var token = value.substring(Authorization.TYPE.length()).trim();
            try {
                token = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
                val credentials = Credentials.of(token.split(":", 2));
                if (!authenticator.authenticate(credentials))
                    throw new AuthorizationException("Invalid credentials with login " + credentials.getToken());

                val ctx = Context.current().withValue(Authorization.CONTEXT_LOGIN_KEY, credentials.getToken());
                return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
            } catch (Throwable throwable) {
                status = Status.UNAUTHENTICATED.withDescription(throwable.getMessage()).withCause(throwable);
            }
        }

        serverCall.close(status, new Metadata());

        return new ServerCall.Listener<ReqT>() {};
    }
}

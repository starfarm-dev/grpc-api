package ru.starfarm.grpc.authorization.credentials;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import lombok.Getter;
import ru.starfarm.grpc.authorization.Authorization;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.Executor;

@Getter
public class BasicCredentials extends CallCredentials {

    protected final String base64key;

    BasicCredentials(String login, String token) {
        base64key = Base64.getEncoder()
                .encodeToString(String.format("%s:%s", login, token).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadata) {
        executor.execute(() -> {
            try {
                Metadata headers = new Metadata();
                headers.put(Authorization.HEADER_KEY, String.format("%s %s", Authorization.TYPE, base64key));
                metadata.apply(headers);
            } catch (Throwable e) {
                metadata.fail(Status.UNAUTHENTICATED.withCause(e));
            }
        });
    }

    @Override
    public void thisUsesUnstableApi() {

    }

    public static BasicCredentials of(String login, String token) {
        return new BasicCredentials(login, token);
    }

    public static BasicCredentials of(Credentials credentials) {
        return of(credentials.getLogin(), credentials.getToken());
    }

}

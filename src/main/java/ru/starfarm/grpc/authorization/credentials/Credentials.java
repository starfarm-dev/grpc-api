package ru.starfarm.grpc.authorization.credentials;

import lombok.Data;

@Data
public class Credentials {

    protected final String login, token;

    public static Credentials of(String[] tokenParts) {
        return of(tokenParts[0], tokenParts[1]);
    }

    public static Credentials of(String login, String token) {
        return new Credentials(login, token);
    }

}

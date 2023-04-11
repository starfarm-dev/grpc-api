package ru.starfarm.grpc.authorization;

import ru.starfarm.grpc.authorization.credentials.Credentials;

@FunctionalInterface
public interface Authenticator {

    boolean authenticate(Credentials credentials);

}

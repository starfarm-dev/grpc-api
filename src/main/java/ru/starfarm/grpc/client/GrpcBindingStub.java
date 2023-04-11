package ru.starfarm.grpc.client;

import io.grpc.stub.AbstractStub;
import lombok.Value;

@Value
public class GrpcBindingStub<S extends AbstractStub<S>> {

    GrpcClient client;
    S stub;

    public S credential() {
        return client.hasCredentials() ? stub.withCallCredentials(client.getCredentials()) : stub;
    }

}

package ru.starfarm.grpc.client;

import io.grpc.*;
import io.grpc.stub.AbstractStub;
import lombok.Value;
import lombok.experimental.Delegate;

import java.util.function.Function;

@Value
public class GrpcClient {

    @Delegate
    ManagedChannel channel;
    CallCredentials credentials;

    GrpcClient(String host, int port, CallCredentials credentials) {
        channel = Grpc.newChannelBuilderForAddress(host, port, InsecureChannelCredentials.create()).build();
        this.credentials = credentials;
    }

    public boolean hasCredentials() {
        return credentials != null;
    }

    public <S extends AbstractStub<S>> GrpcBindingStub<S> stub(Function<Channel, S> stub) {
        return new GrpcBindingStub<>(this, stub.apply(channel));
    }

    public static GrpcClient connect(String host, int port, CallCredentials credentials) {
        return new GrpcClient(host, port, credentials);
    }

    public static GrpcClient connect(String host, int port) {
        return connect(host, port, null);
    }

}

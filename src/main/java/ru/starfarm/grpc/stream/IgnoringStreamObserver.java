package ru.starfarm.grpc.stream;

import io.grpc.stub.StreamObserver;

public class IgnoringStreamObserver<T> implements StreamObserver<T> {
    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {

    }
}

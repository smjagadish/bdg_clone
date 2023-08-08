package com.baeldung.grpc.order;

import io.grpc.stub.StreamObserver;

public class OrderClientAsync implements StreamObserver {
    @Override
    public void onNext(Object value) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {

    }
}

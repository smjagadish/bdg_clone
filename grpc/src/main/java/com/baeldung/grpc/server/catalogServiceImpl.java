package com.baeldung.grpc.server;

import com.baeldung.grpc.catalogRequest;
import com.baeldung.grpc.catalogResponse;
import com.baeldung.grpc.catalogServiceGrpc;
import io.grpc.stub.StreamObserver;

public class catalogServiceImpl extends catalogServiceGrpc.catalogServiceImplBase {
    @Override
    public void queryCatalog(catalogRequest request, StreamObserver<catalogResponse> responseObserver) {
        System.out.println("request from client for query catalog with "+ request.getProductCode() + "and" +request.getProductVersion());
        catalogResponse response = catalogResponse.newBuilder().setResponse(true).setValid(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

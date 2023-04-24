package com.baeldung.grpc.client;

import com.baeldung.grpc.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
            .usePlaintext()
            .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub 
          = HelloServiceGrpc.newBlockingStub(channel);

        HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
            .setFirstName("Baeldung")
            .setLastName("gRPC")
            .build());

        System.out.println("Response received from server:\n" + helloResponse);

        catalogServiceGrpc.catalogServiceBlockingStub stub2 = catalogServiceGrpc.newBlockingStub(channel);
        catalogResponse response = stub2.queryCatalog(catalogRequest.newBuilder().setProductCode("x3drf").setProductVersion("v1").build());
        System.out.println("Response received from server for query catalog :\n" + response);

        channel.shutdown();
    }
}

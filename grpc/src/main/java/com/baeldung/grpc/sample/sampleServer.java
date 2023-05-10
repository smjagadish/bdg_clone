package com.baeldung.grpc.sample;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;

import java.io.IOException;

public class sampleServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8191).addService(ServerInterceptors.intercept(new sampleServiceImpl(), new sampleServerRespInterceptor(),new sampleServerInterceptor(),new sampleServerInterceptor2()))
                .build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
        server.awaitTermination();

    }
}

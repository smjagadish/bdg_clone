package com.baeldung.grpc.sample;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;

import java.io.IOException;

import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyServerBuilder;
import nl.altindag.ssl.SSLFactory;
import io.netty.handler.ssl.SslContext;
import nl.altindag.ssl.util.NettySslUtils;

public class sampleServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        SSLFactory sslFactory = SSLFactory.builder()
                .withIdentityMaterial("keystore", "test123".toCharArray())
                .withTrustMaterial("truststore", "test123".toCharArray())
                .withNeedClientAuthentication()
                .build();
        SslContext sslContext = GrpcSslContexts.configure(NettySslUtils.forServer(sslFactory)).build();



        Server server = NettyServerBuilder.forPort(8191).addService(ServerInterceptors.intercept(new sampleServiceImpl(), new sampleServerEnInterceptor(),new sampleServerRespInterceptor(),new sampleServerInterceptor(),new sampleServerInterceptor2()))
                .sslContext(sslContext)
                .build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
        server.awaitTermination();

    }
}

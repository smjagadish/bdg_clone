package com.baeldung.grpc.sample;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.util.NettySslUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public class sampleClient {
    public static void main(String[] args) throws SSLException {
        SSLFactory sslFactory = SSLFactory.builder()
                .withIdentityMaterial("keystore", "test123".toCharArray())
                .withTrustMaterial("truststore", "test123".toCharArray())

                .build();
        SslContext sslContext = GrpcSslContexts.configure(NettySslUtils.forClient(sslFactory)).build();

        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost",8191)
                .intercept( new sampleClientRespInterceptor(),new sampleClientInterceptor2(),new sampleClientInterceptor())
                .sslContext(sslContext)
                .build();
        sampleReqDS reqDS = sampleReqDS.newBuilder().setSerialNum(1)
                .setReqItem("blades")
                .build();
        sampleQueryGrpc.sampleQueryBlockingStub stub = sampleQueryGrpc.newBlockingStub(channel);
        sampleQueryGrpc.sampleQueryStub asyncstub = sampleQueryGrpc.newStub(channel);
      //  asyncstub.unarySampleQuery(reqDS,new sampleClientAsync());

        sampleResDS resDS = stub.unarySampleQuery(reqDS);
        switch(resDS.getRespCase())
        {
            case ERESP:
                System.out.println("ran into error condition"+" "+resDS.getEresp().getDesc()+" "+resDS.getEresp().getCode().getNumber());
                break;

            case SRESP:
                System.out.println("success case"+" "+resDS.getSresp().getAckNum()+" "+resDS.getSresp().getStatus());
                break;
        }
        channel.shutdown();
    }

}

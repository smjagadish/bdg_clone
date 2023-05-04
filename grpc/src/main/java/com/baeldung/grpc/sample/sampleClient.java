package com.baeldung.grpc.sample;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class sampleClient {
    public static void main(String[] args)
    {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",8191)
                .usePlaintext()
                .intercept(new sampleClientInterceptor())
                .build();
        sampleReqDS reqDS = sampleReqDS.newBuilder().setSerialNum(1)
                .setReqItem("blades")
                .build();
        sampleQueryGrpc.sampleQueryBlockingStub stub = sampleQueryGrpc.newBlockingStub(channel);
        sampleQueryGrpc.sampleQueryStub asyncstub = sampleQueryGrpc.newStub(channel);
        asyncstub.unarySampleQuery(reqDS,new sampleClientAsync());

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
    }
}

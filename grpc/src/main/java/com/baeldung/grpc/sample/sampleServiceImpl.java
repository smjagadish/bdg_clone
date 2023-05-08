package com.baeldung.grpc.sample;

import io.grpc.stub.StreamObserver;

public class sampleServiceImpl extends sampleQueryGrpc.sampleQueryImplBase {
    @Override
    public void unarySampleQuery(sampleReqDS request, StreamObserver<sampleResDS> responseObserver) {

        if(request.getSerialNum()==1)
        {
            System.out.println("the commodity is"+request.getReqItem());
            successResponse sresp = successResponse.newBuilder().setStatus("in stock")
                    .setAckNum(100)
                    .build();
            sampleResDS resDS = sampleResDS.newBuilder().setSresp(sresp).build();
            responseObserver.onNext(resDS);
        }
        else
        {
            errorResponse eresp = errorResponse.newBuilder().setCode(errorCode.PRD_UNAVL)
                    .setDesc("sorry, unavailable")
                    .build();
            sampleResDS resDS = sampleResDS.newBuilder().setEresp(eresp).build();
            responseObserver.onNext(resDS);
        }
        responseObserver.onCompleted();
    }
}

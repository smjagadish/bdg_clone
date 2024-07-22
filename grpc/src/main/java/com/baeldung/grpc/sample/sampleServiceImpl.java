package com.baeldung.grpc.sample;

import io.grpc.Context;
import io.grpc.ServerInterceptors;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class sampleServiceImpl extends sampleQueryGrpc.sampleQueryImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(sampleServiceImpl.class);

    @Override
    public void unarySampleQuery(sampleReqDS request, StreamObserver<sampleResDS> responseObserver) {
        Context prev = null;
        Context ctx = null;
        try {

            if (request.getSerialNum() == 1) {
                LOGGER.info("server main thread");
                //context data retrieval and printing
                String data = ((String) Constants.auth_token.get());
                LOGGER.info("printing context set in interceptor" + " " + data);
                System.out.println("the commodity is" + request.getReqItem());
                successResponse sresp = successResponse.newBuilder().setStatus("in stock")
                        .setAckNum(100)
                        .build();
                sampleResDS resDS = sampleResDS.newBuilder().setSresp(sresp).build();
                //context data population for usage in trailer

                 ctx = Context.current().withValue(Constants.resp_token, "qwertymaniac");
                 prev = ctx.attach();

                responseObserver.onNext(resDS);
            } else {
                errorResponse eresp = errorResponse.newBuilder().setCode(errorCode.PRD_UNAVL)
                        .setDesc("sorry, unavailable")
                        .build();
                sampleResDS resDS = sampleResDS.newBuilder().setEresp(eresp).build();
                responseObserver.onNext(resDS);
            }

            responseObserver.onCompleted();
        }
        finally {
            ctx.detach(prev);
        }
    }

}

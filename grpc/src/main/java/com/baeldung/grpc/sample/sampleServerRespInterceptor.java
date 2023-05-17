package com.baeldung.grpc.sample;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class sampleServerRespInterceptor implements ServerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(sampleServerRespInterceptor.class);


    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        LOGGER.info("inside server resp modif interceptor - advanced");

        return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {

            @Override
            public void request(int numMessages) {
                super.request(numMessages);
            }

            @Override
            public void sendMessage(RespT message) {
                LOGGER.info("inside server resp modif interceptor - advanced");

                System.out.print("intercepting server resp to change content");
               sampleResDS msg = (sampleResDS) message;
              /* switch (msg.getRespCase())
                {
                    case SRESP:
                        successResponse sresp = msg.getSresp();
                        message = (RespT) msg.toBuilder().setSresp(sresp.toBuilder().setAckNum(200)
                                .setStatus("in stock")
                                .build());

                        break;
                    case ERESP:
                        // do nothing
                        break;
                }*/
                super.sendMessage(message);
            }

            @Override
            public void close(Status status, Metadata trailers) {
                super.close(status, trailers);
            }

        },headers);
    }
}

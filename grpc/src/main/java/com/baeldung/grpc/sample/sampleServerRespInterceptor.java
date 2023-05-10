package com.baeldung.grpc.sample;

import io.grpc.*;

public class sampleServerRespInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendMessage(RespT message) {
                System.out.print("intercepting server resp to change content");
               sampleResDS msg = (sampleResDS) message;
              /*  switch (msg.getRespCase())
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

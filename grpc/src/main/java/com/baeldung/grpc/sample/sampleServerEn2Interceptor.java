package com.baeldung.grpc.sample;

import io.grpc.*;

public class sampleServerEn2Interceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(serverCallHandler.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
            @Override
            public void sendMessage(RespT message) {
                System.out.println("a no-op server call on the way-out");
                super.sendMessage(message);
            }
        },metadata)) {
            @Override
            public void onMessage(ReqT message) {
                System.out.println("a no-op server call on the way-in");
                super.onMessage(message);
            }
        };
    }
}

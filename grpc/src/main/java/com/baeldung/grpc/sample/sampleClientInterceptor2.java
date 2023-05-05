package com.baeldung.grpc.sample;

import io.grpc.*;

public class sampleClientInterceptor2 implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method,callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {

                System.out.println("second interception of client call to inject header");

                headers.put(Metadata.Key.of("x-fwd-headers",Metadata.ASCII_STRING_MARSHALLER),"none");
                super.start(responseListener, headers);
            }
        };

    }
}

package com.baeldung.grpc.sample;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class sampleServerInterceptor2 implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        System.out.println("second server interceptor and no modifications");

        headers.put(Metadata.Key.of("injected-key",Metadata.ASCII_STRING_MARSHALLER),"working");
        return next.startCall(call, headers);
    }
}

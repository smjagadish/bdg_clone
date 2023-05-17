package com.baeldung.grpc.sample;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class sampleServerInterceptor2 implements ServerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(sampleServerInterceptor2.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        LOGGER.info("inside server interceptor #2");
        System.out.println("second server interceptor and no modifications");

        headers.put(Metadata.Key.of("injected-key",Metadata.ASCII_STRING_MARSHALLER),"working");
        return next.startCall(call, headers);
    }

}

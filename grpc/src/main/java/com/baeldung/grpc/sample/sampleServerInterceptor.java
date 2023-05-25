package com.baeldung.grpc.sample;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class sampleServerInterceptor implements ServerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(sampleServerInterceptor.class);
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        MDC.put("transaction.id","CXD456");
        LOGGER.info("inside server intercepto#1");
        System.out.println("first server interceptor and no modifications");
        MDC.clear();
        return next.startCall(call,headers);
    }

}

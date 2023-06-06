package com.baeldung.grpc.sample;

import io.grpc.*;
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
        // ctx retrieval . will not set again , but will check in impl
        LOGGER.info("ctx retrieval in interceptor");
        System.out.println((String)Constants.auth_token.get());
        return next.startCall(call,headers);
    }


}

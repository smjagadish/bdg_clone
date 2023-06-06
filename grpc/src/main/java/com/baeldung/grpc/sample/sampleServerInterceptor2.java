package com.baeldung.grpc.sample;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class sampleServerInterceptor2 implements ServerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(sampleServerInterceptor2.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        LOGGER.info("inside server interceptor #2");
        System.out.println("second server interceptor and no modifications");

        headers.put(Metadata.Key.of("injected-key",Metadata.ASCII_STRING_MARSHALLER),"working");
        // retrieving context info set as MD in client side
        String token = headers.get(Metadata.Key.of("auth_token", Metadata.ASCII_STRING_MARSHALLER));
        Context ctx = Context.current().withValue(Constants.auth_token,token);
        return Contexts.interceptCall(ctx,call,headers,next);
    }

}

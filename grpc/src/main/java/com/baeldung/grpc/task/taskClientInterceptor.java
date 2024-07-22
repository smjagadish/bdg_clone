package com.baeldung.grpc.task;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class taskClientInterceptor implements ClientInterceptor {

    static final Logger logger = LoggerFactory.getLogger(taskClientInterceptor.class);
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
       logger.info("no-op logging interceptor for the client. gets invoked when msg is being sent out");
       return next.newCall(method,callOptions);
    }
}


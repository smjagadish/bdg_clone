package com.baeldung.grpc.task;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class taskClientMsgInterceptor implements ClientInterceptor {
    static final Logger logger = LoggerFactory.getLogger(taskClientMsgInterceptor.class);
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method,callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                super.start(responseListener, headers);
            }

            @Override
            public void sendMessage(ReqT message) {
                // message modification
                logger.info("wrong actor is now modifying all the messages");
                if(message.getClass()== requestData.class)
                {
                    requestData rdata = (requestData) message;
                    rdata=rdata.toBuilder()
                            .setTaskID(8956)
                        .setTaskName("state actor modified message")
                        .setTData(taskData.newBuilder().setTaskPrio(8).setTaskCategory("tampered_stream").build())
                        .build();
                   message = (ReqT) rdata;
                }
                super.sendMessage(message);
            }

        };
    }
}

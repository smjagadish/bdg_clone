package com.baeldung.grpc.sample;

import io.grpc.*;

public class sampleClientRespInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method,callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onMessage(RespT message) {
                        System.out.println("response interception");
                        if(message.getClass()==sampleResDS.class)
                        {
                            sampleResDS obj= (sampleResDS)message;
                            switch(obj.getRespCase())
                            {
                                case ERESP:
                                    System.out.println("error case resp intercept");
                                    break;
                                case SRESP:
                                    System.out.println("success case resp intercept");
                                    break;

                            }
                        }
                        super.onMessage(message);
                    }
                },headers);
            }
        };
    }
}

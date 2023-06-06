package com.baeldung.grpc.sample;

import io.grpc.*;

public class sampleClientInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method,callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                System.out.println("intercepting client call to inject header");
                headers.put(Metadata.Key.of("auth_token",Metadata.ASCII_STRING_MARSHALLER),"fhskhfkshfkHjkzxchkhkjhc");
                // Testing the context
                Context ctx = Context.current().withValue(Context.key("temp"),"ctx value");
                // looks like i cannot pass this context between interceptors and/or impl
                // i must investigate this further , until then use metadata for populating header data
                super.start(responseListener, headers);
            }

            @Override
            public void sendMessage(ReqT message) {

                if(message.getClass()==sampleReqDS.class)
                {
                    System.out.println("override request body ");
                    sampleReqDS modif_message = (sampleReqDS) message;

                    modif_message = modif_message.toBuilder().setReqItem("server_blades").setSerialNum(1).build();
                    message = (ReqT) modif_message;

                }
                super.sendMessage(message);
            }
        };
    }

}

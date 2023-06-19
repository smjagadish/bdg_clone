package com.baeldung.grpc.sample;

import io.grpc.*;

public class sampleServerEnInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        //return serverCallHandler.startCall(serverCall, metadata);

return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(serverCallHandler.startCall(serverCall,metadata)) {
    @Override
    public void onMessage(ReqT message) {
        System.out.println("crazy crazy impl");
        super.onMessage(message);
    }

    @Override
    protected ServerCall.Listener<ReqT> delegate() {
        return super.delegate();
    }

    @Override
    public void onReady() {
        System.out.println("svr is now ready");
        super.onReady();
    }

};
    }
}

package com.baeldung.grpc.sample;

import io.grpc.stub.StreamObserver;

public class sampleClientAsync implements StreamObserver<sampleResDS> {
    @Override
    public void onNext(sampleResDS value) {

        switch(value.getRespCase())
        {
            case ERESP:
                System.out.println("async ran into error condition"+" "+value.getEresp().getDesc()+" "+value.getEresp().getCode().getNumber());
                break;

            case SRESP:
                System.out.println("async success case"+" "+value.getSresp().getAckNum()+" "+value.getSresp().getStatus());
                break;
        }
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {

    }
}

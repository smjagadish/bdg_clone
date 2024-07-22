package com.baeldung.grpc.task;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class bidiStreamResp implements StreamObserver<responseData> {
    private CountDownLatch latch;
    private Logger logger = LoggerFactory.getLogger(bidiStreamResp.class);
    bidiStreamResp (CountDownLatch latch)
    {
        this.latch = latch;
    }
    @Override
    public void onNext(responseData value) {
        logger.info("parsing the response sent by server");
        switch (value.getResCase())
        {
            case TRESP:
                logger.info("successful response details:"+value.getTresp().getTaskAdmit() +" "+value.getTresp().getTaskNotes()+" "+value.getTresp().getExtraData().getTaskDescription());
                break;
            case ERESP:
                logger.info("error response, will not dump it");
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(Throwable t) {
        logger.info("bidi stream errored , do not continue");
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        logger.info("bidi stream closure complete");
        latch.countDown();

    }
}

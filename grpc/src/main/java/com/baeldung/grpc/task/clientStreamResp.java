package com.baeldung.grpc.task;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class clientStreamResp implements StreamObserver<responseData> {
    private final Logger logger = LoggerFactory.getLogger(clientStreamResp.class);
    private CountDownLatch latch;

    clientStreamResp(CountDownLatch latch)
    {
        this.latch = latch;
    }
    @Override
    public void onNext(responseData value) {
        logger.info("processing the response for client streaming");
        switch (value.getResCase()) {
            case ERESP:
                logger.info("stream unsuccessful ,ignoring the error data");
                break;
            case TRESP:
                logger.info("stream successful");
                logger.info("task created with" + ":" + value.getTresp().getTaskNotes());
                break;
            default:
                logger.info("must not come here");
                break;
        }
    }

    @Override
    public void onError(Throwable t) {
        logger.info("must not come here");
        Metadata mtd = Status.trailersFromThrowable(t);
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        logger.info("stream ends");
        latch.countDown();

    }
}

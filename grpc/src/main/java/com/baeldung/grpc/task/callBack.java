package com.baeldung.grpc.task;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class callBack implements StreamObserver<responseData> {
    private static Logger LOGGER = LoggerFactory.getLogger(callBack.class);

    @Override
    public void onNext(responseData value) {
        switch (value.getResCase()) {
            case ERESP:
                LOGGER.info("encountered error");
                errorResponse eresp = value.getEresp();
                LOGGER.info("error details" + eresp.getReason());
                break;

            case TRESP:
                LOGGER.info("successful response");
                taskResponse tresp = value.getTresp();
                LOGGER.info("successful response details" + " " + "task_admit:" + tresp.getTaskAdmit() + " " + "task_notes:" + tresp.getTaskNotes() + " "
                        + " task_extra_data:" + tresp.getExtraData().getTaskDescription());
                break;

            default:
                LOGGER.info("err - shouldn't happen");
                break;
        }
    }

    @Override
    public void onError(Throwable t) {
        Status st = Status.fromThrowable(t);
        Metadata md = Status.trailersFromThrowable(t);
        LOGGER.info("err - shouldn't happen");
    }

    @Override
    public void onCompleted() {
        LOGGER.info("all done");
    }
}

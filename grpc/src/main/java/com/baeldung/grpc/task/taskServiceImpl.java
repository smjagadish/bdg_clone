package com.baeldung.grpc.task;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class taskServiceImpl extends createTaskGrpc.createTaskImplBase{
    private static Logger logger = LoggerFactory.getLogger(taskServiceImpl.class);

    @Override
    public void unaryCreate(requestData request, StreamObserver<responseData> responseObserver) {
        logger.info("unary create for task service");
        // success case
        taskResponse tResp = taskResponse.newBuilder()
                .setTaskAdmit(true)
                .setTaskNotes("task" + request.getTaskName() + "created")
                .setExtraData(taskSupport.newBuilder().setTaskDescription(" a unary task response object for successful case").setTaskModification(true).build())
                .build();
        // statically successful now , will be modified later
        responseData resp = responseData.newBuilder().setTresp(tResp).build();
        // stream ends
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<requestData> clientStream(StreamObserver<responseData> responseObserver) {
        return new StreamObserver<requestData>() {
            @Override
            public void onNext(requestData value) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };
    }

    @Override
    public void serverStream(requestData request, StreamObserver<responseData> responseObserver) {
        super.serverStream(request, responseObserver);
    }

    @Override
    public StreamObserver<requestData> bidiStream(StreamObserver<responseData> responseObserver) {
        return super.bidiStream(responseObserver);
    }
}

package com.baeldung.grpc.task;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class taskServiceImpl extends createTaskGrpc.createTaskImplBase {
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
        logger.info("starting a client side streaming use case");
        return new StreamObserver<requestData>() {
            @Override
            public void onNext(requestData value) {
                logger.info("dumping the stream data contents");
                logger.info("task recieved with id" + ":"+value.getTaskID() +"and name:"+value.getTaskName());

            }

            @Override
            public void onError(Throwable t) {
                logger.info("errored");

            }

            @Override
            public void onCompleted() {
                logger.info("the client stream has ended and now sending the result");
                taskResponse tResp = taskResponse.newBuilder()
                        .setTaskAdmit(true)
                        .setTaskNotes(" client streaming response task created")
                        .setExtraData(taskSupport.newBuilder().setTaskDescription(" client side streaming for successful case").setTaskModification(true).build())
                        .build();
                // statically successful now , will be modified later
                responseData resp = responseData.newBuilder().setTresp(tResp).build();
                // stream ends
                // remove later - delay for otel
                try {
                    Thread.sleep(200000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                responseObserver.onNext(resp);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void serverStream(requestData request, StreamObserver<responseData> responseObserver) {
        logger.info("server side streaming");
        // success case
        for (int i = 0; i < 10; i++) {
            taskResponse tResp = taskResponse.newBuilder()
                    .setTaskAdmit(true)
                    .setTaskNotes("task" + request.getTaskName() + "created")
                    .setExtraData(taskSupport.newBuilder().setTaskDescription(" server side streaming for successful case").setTaskModification(true).build())
                    .build();
            // statically successful now , will be modified later
            responseData resp = responseData.newBuilder().setTresp(tResp).build();
            // stream ends
            responseObserver.onNext(resp);
        }
        responseObserver.onCompleted();

    }

    @Override
    public StreamObserver<requestData> bidiStream(StreamObserver<responseData> responseObserver) {
        logger.info("bidirectional side streaming");
        return new StreamObserver<requestData>() {
            @Override
            public void onNext(requestData value) {
                logger.info("parsing the bidirectional stream");
                logger.info("recieved info" + " - " + "taskid:" + value.getTaskID() + "task_name:" + value.getTaskName());
                logger.info("parsed and will pick next stream");
                logger.info("server is now responding to client stream");
                // success case
                taskResponse tResp = taskResponse.newBuilder()
                        .setTaskAdmit(true)
                        .setTaskNotes("requested task is created")
                        .setExtraData(taskSupport.newBuilder().setTaskDescription(" a bidi stream task response object for successful case").setTaskModification(true).build())
                        .build();
                // statically successful now , will be modified later
                responseData resp = responseData.newBuilder().setTresp(tResp).build();
                // stream still continues
                responseObserver.onNext(resp);

            }

            @Override
            public void onError(Throwable t) {
                logger.info("encountered error, will attempt a local cleanup only");

            }

            @Override
            public void onCompleted() {
                logger.info("bidirectional stream closure initiated ");
                responseObserver.onCompleted();
            }
        };
    }
}

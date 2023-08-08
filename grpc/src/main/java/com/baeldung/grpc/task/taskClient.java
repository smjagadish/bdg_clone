package com.baeldung.grpc.task;

import com.baeldung.grpc.taskextn.taskExtnProto;
import com.google.protobuf.DescriptorProtos;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class taskClient {
    private static Logger LOGGER = LoggerFactory.getLogger(taskClient.class);

    public static void main(String[] args) throws InterruptedException {
        callBack cb = new callBack();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8787)
                .usePlaintext()
                .build();
        createTaskGrpc.createTaskBlockingStub stub = createTaskGrpc.newBlockingStub(channel);
        createTaskGrpc.createTaskStub asyncstub = createTaskGrpc.newStub(channel);
        // unary request starts
        requestData rdata = requestData.newBuilder()
                .setTaskID(9876)
                .setTaskName("unary streaming in a blocking stub")
                .setTData(taskData.newBuilder().setTaskPrio(1).setTaskCategory("block_stream").build())
                .build();
        int timeout = cMessage.getDescriptor().getOptions().getExtension(taskExtnProto.timeout);
        responseData res = stub.unaryCreate(rdata);
        switch (res.getResCase()) {
            case ERESP:
                LOGGER.info("encountered error");
                errorResponse eresp = res.getEresp();
                LOGGER.info("error details" + eresp.getReason());
                break;

            case TRESP:
                LOGGER.info("successful response");
                taskResponse tresp = res.getTresp();
                LOGGER.info("successful response details" + " " + "task_admit:" + tresp.getTaskAdmit() + " " + "task_notes:" + tresp.getTaskNotes() + " "
                        + " task_extra_data:" + tresp.getExtraData().getTaskDescription());
                break;

            default:
                LOGGER.info("err - shouldn't happen");
                break;
        }
        // unary request ends
        // server streaming starts
        requestData rserdata = requestData.newBuilder()
                .setTaskID(1485)
                .setTaskName("server streaming in async stub")
                .setTData(taskData.newBuilder().setTaskPrio(1).setTaskCategory("server_stream").build())
                .build();
        asyncstub.serverStream(rserdata, cb);
        // server streaming ends. result processed async
        // add delay , else the asyncstub exec cannot be followed - can be ignored if we have client streaming and/or bidi streaming as these add/induce delay
        // client streaming starts
        final CountDownLatch counter = new CountDownLatch(1);
        StreamObserver<requestData> req =  asyncstub.clientStream(new clientStreamResp(counter));
        try {
            for (int i = 0; i < 10; i++) {
                if (counter.getCount() == 0)
                    return;
                requestData reqdata = requestData.newBuilder()
                        .setTaskID(1485)
                        .setTaskName("client streaming in async stub")
                        .setTData(taskData.newBuilder().setTaskPrio(1).setTaskCategory("client_stream").build())
                        .build();
                Thread.sleep(10000);
                req.onNext(reqdata);
            }
            req.onCompleted();
        }
        catch(RuntimeException e)
        {
            counter.countDown();
        }

        counter.await(200, TimeUnit.SECONDS);
        channel.shutdown();
    }
}

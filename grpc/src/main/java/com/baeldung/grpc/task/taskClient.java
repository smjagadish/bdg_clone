package com.baeldung.grpc.task;

import com.google.protobuf.DescriptorProtos;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class taskClient {
    private static Logger LOGGER = LoggerFactory.getLogger(taskClient.class);
    public static void main (String[] args)
    {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",8787)
                .usePlaintext()
                .build();
        createTaskGrpc.createTaskBlockingStub stub = createTaskGrpc.newBlockingStub(channel);

        requestData rdata = requestData.newBuilder()
                .setTaskID(9876)
                .setTaskName("unary streaming in a blocking stub")
                .setTData(taskData.newBuilder().setTaskPrio(1).setTaskCategory("block_stream").build())
                .build();
        
        responseData res = stub.unaryCreate(rdata);
        switch (res.getResCase())
        {
            case ERESP:
                LOGGER.info("encountered error");
            errorResponse eresp = res.getEresp();
            LOGGER.info("error details" + eresp.getReason());
            break;

            case TRESP:
                LOGGER.info("successful response");
            taskResponse tresp = res.getTresp();
            LOGGER.info("successful response details" + " " +"task_admit:" +tresp.getTaskAdmit() + " "+ "task_notes:"+tresp.getTaskNotes()+ " "
            +" task_extra_data:"+tresp.getExtraData().getTaskDescription());
            break;

            default:
                LOGGER.info("err - shouldn't happen");
                break;
        }
        channel.shutdown();
    }
}

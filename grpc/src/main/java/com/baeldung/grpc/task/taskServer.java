package com.baeldung.grpc.task;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class taskServer {
    private static Logger LOGGER = LoggerFactory.getLogger(taskServer.class);
    public static  void main (String []args)
    {
        Server server = ServerBuilder.forPort(8787)
                .addService(new taskServiceImpl())
                .build();
        try {

            server.start();
            LOGGER.info("server started");
            server.awaitTermination();
        }
        catch (Exception e){
           LOGGER.info("server encountered exception");
           }
        finally {
            LOGGER.info("server will now shutdown");
            if(server!=null)
            server.shutdown();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                LOGGER.info("server will now shutdown");
                if(server!=null)
                    server.shutdown();
            }
        });
    }
}

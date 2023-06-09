package com.baeldung.grpc.server;

import com.baeldung.grpc.HelloError;
import com.baeldung.grpc.HelloRequest;
import com.baeldung.grpc.HelloResponse;
import com.baeldung.grpc.HelloServiceGrpc.HelloServiceImplBase;

import com.baeldung.grpc.errorhandling.AdditionalError;
import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.HashMap;

public class HelloServiceImpl extends HelloServiceImplBase {

    @Override
    public void hello(

      HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        System.out.println("Request received from client:\n" + request);

        String greeting = new StringBuilder().append("Hello, ")
            .append(request.getFirstName())
            .append(" ")
            .append(request.getLastName())
            .toString();
        boolean st = false;



        //  standard grpc error handling
        // The client sets a deadline for this specific rpc call alone
        // checking in server side if context is closed due to expired deadline so that we can avoid the processing
        if (Context.current().isCancelled()) {
            responseObserver.onError(Status.CANCELLED.withDescription("Cancelled by client").asRuntimeException());
            return;
        }

        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();
        if (request.getFirstName().equalsIgnoreCase("Baeldung"))
{


    Status status = Status.INVALID_ARGUMENT.withDescription("wrong first name");
    // cascading the exception to the client by calling response.onError
    // note that with response.onError , the onNext and onCompleted signals are null and void
    // i guess the server end of the stream is also closed when this happens
    responseObserver.onError(status.asRuntimeException());
}
        //  standard grpc error handling with metadata

        if (request.getFirstName().equalsIgnoreCase("Bang"))
        {


            Status status = Status.INVALID_ARGUMENT.withDescription("wrong first name, will provide metadata");
            // cascading the exception to the client by calling response.onError
            // additional metadata added with error info
            // note that with response.onError , the onNext and onCompleted signals are null and void
            // i guess the server end of the stream is also closed when this happens
            Metadata mtd = new Metadata();
            mtd.put(Metadata.Key.of("code",Metadata.ASCII_STRING_MARSHALLER),"E1234");
            mtd.put(Metadata.Key.of("cause",Metadata.ASCII_STRING_MARSHALLER),"willfull termination");
            responseObserver.onError(status.asRuntimeException(mtd));
        }

        // google grpc error handling with custom metadata

        else if (request.getFirstName().equalsIgnoreCase("Big"))
        {
            // needs a placeholder proto object to store the error info
            HelloError er = HelloError.newBuilder().setAddError("inner error").build();
            com.google.rpc.Status status = com.google.rpc.Status.newBuilder().setCode(Code.INTERNAL.getNumber())
                    .setMessage("sorry encountered internal error")
                    .addDetails(Any.pack(er))
                    .build();
            // slightly different technique to convert google rpc error into exception
            responseObserver.onError(StatusProto.toStatusException(status));
        }

        // google grpc error handling with proprietary metadata

        else if(request.getFirstName().equalsIgnoreCase("Beg"))
        {
            // placeholder proto object built using the prop model
            ErrorInfo einfo = ErrorInfo.newBuilder()
                    .setReason("prop error")
                    .setDomain("DMT")
                    .putAllMetadata(Collections.singletonMap("k1","val1"))
                    .build();
            com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
                    .setCode(Code.INTERNAL_VALUE)
                    .setMessage("prop data ")
                    .addDetails(Any.pack(einfo))
                    .build();
            responseObserver.onError(StatusProto.toStatusException(status));
        }
        else {
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}

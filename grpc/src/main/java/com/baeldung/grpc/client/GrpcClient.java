package com.baeldung.grpc.client;

import com.baeldung.grpc.*;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.*;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.protobuf.StatusProto;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GrpcClient {
    public static void main(String[] args) throws InterruptedException, InvalidProtocolBufferException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
            .usePlaintext()
            .proxyDetector(new myProxy())
            .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub 
          = HelloServiceGrpc.newBlockingStub(channel);
try {
    // below invocation will result in the server raising an exception without any metadata
    // snippet below shows the possibility to use a deadline (aka timeout) . server errors out on the rpc if it cannot respond within the deadline
    // the deadline i believe is an app level construct . possible that the call completes quick in app but being buffered in netty or other lower layer
   /* HelloResponse helloResponse = stub.withDeadlineAfter(100,TimeUnit.MILLISECONDS).hello(HelloRequest.newBuilder()
            .setFirstName("Baeldung")
            .setLastName("gRPC")
            .build());*/

     HelloResponse helloResponse = stub.withDeadlineAfter(2000,TimeUnit.MILLISECONDS).hello(HelloRequest.newBuilder()
            .setFirstName("Baeldung")
            .setLastName("gRPC")
            .build());

    System.out.println("Response received from server:\n" + helloResponse);

}
catch (StatusRuntimeException e)
{
 System.out.println("errored due to exception"+" "+e.getMessage()+" "+"and status is"+e.getStatus());
}

try {
    // below invocation will result in the server raising an exception with metadata
    HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
            .setFirstName("Bang")
            .setLastName("gRPC")
            .build());
}
catch (StatusRuntimeException e)
{
    System.out.println("errored due to exception"+" "+e.getMessage()+" "+"and status is"+e.getStatus());
    // get the trailing metadata first
    Metadata trailers = e.getTrailers();
    // get key info
    Set<String> eset = trailers.keys();

    for (String key:eset)
    {
        // for each key get corresponding md
        Metadata.Key<String> k = Metadata.Key.of(key,Metadata.ASCII_STRING_MARSHALLER);
        // retrieve data
        System.out.println("error med"+" "+trailers.get(k));

    }
}
try
{
       // below invocation will result in server raising exception using google rpc impl with custom metadata
    HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
            .setFirstName("Big")
            .setLastName("gRPC")
            .build());
}
catch (StatusRuntimeException e)
{
    // different technique to get the google status object
    Status status = StatusProto.fromThrowable(e);
    System.out.println("google rpc exception with "+ " "+status.getCode() + "and message"+" "+status.getMessage());
    HelloError er = null;
    for (Any any : status.getDetailsList())
    {
        if(!(any.is(HelloError.class)))
            continue;
        er = any.unpack(HelloError.class);
     System.out.println("extra info"+" "+er.getAddError());

    }
}

try
{
    // below invocation will trigger google rpc error handling with prop error model
    HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
            .setFirstName("Beg")
            .setLastName("gRPC")
            .build());
}
catch (StatusRuntimeException e)
{
    Status status = StatusProto.fromThrowable(e);
    System.out.println("google rpc exception for prop data with "+ " "+status.getCode() + "and message"+" "+status.getMessage());
    for ( Any any : status.getDetailsList())
    {
        if(!(any.is(ErrorInfo.class)))
            continue;
        ErrorInfo einfo = any.unpack(ErrorInfo.class);
        System.out.println("extra info for prop data "+ " "+einfo.getDomain()+" "+einfo.getMetadataMap().get("k1"));
    }
}


        catalogServiceGrpc.catalogServiceBlockingStub stub2 = catalogServiceGrpc.newBlockingStub(channel);
        catalogResponse response = stub2.queryCatalog(catalogRequest.newBuilder().setProductCode("x3drf").setProductVersion("v1").build());
        System.out.println("Response received from server for query catalog :\n" + response);

        channel.shutdown();
    }
}

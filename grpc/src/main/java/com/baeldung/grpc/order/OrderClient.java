package com.baeldung.grpc.order;

import com.google.rpc.Status;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class OrderClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                .usePlaintext()
                .build();
        orderServiceProviderGrpc.orderServiceProviderBlockingStub stub = orderServiceProviderGrpc.newBlockingStub(channel);
        orderServiceProviderGrpc.orderServiceProviderStub asyncStub = orderServiceProviderGrpc.newStub(channel);
        Order ord = Order.newBuilder().setProductCode("PF4RD").setProductName("Router-6673").setProductQty(10).setPrice(98.56)
                .setProductInfo(info.newBuilder().setProductType("nw gear").setProductBatch("TG5T").build()).build();
        // server side streaming
        Iterator<ProcessOrder> pr = stub.serverSideStreamingGetOrders(ord);
        while (pr.hasNext())
        {
            System.out.println("streaming results from server");
            ProcessOrder obj = pr.next();
            System.out.println("status:" + " "+obj.getStatus());
            System.out.println("qty:" + " "+ obj.getInvQty());
            System.out.println("info:"+" "+obj.getInfo().getPromoCode()+" "+obj.getInfo().getInDemand());
        }
        // client side streaming
        Order cord = Order.newBuilder().setProductName("Router-AL").setProductQty(100).setPrice(128.56).setProductCode("TG76G")
                .setProductInfo(info.newBuilder().setProductType("switch gear").setProductBatch("YHG6T").build()).build();
        final CountDownLatch finishLatch = new CountDownLatch(1);

        StreamObserver<ProcessOrder> respObserver = new StreamObserver<ProcessOrder>() {
            @Override
            public void onNext(ProcessOrder value) {
             System.out.println(" final  response for client streaming ");
             System.out.println("status:"+" "+value.getStatus());
             System.out.println("qty:"+" "+ value.getInvQty());
             System.out.println("info:"+" "+value.getInfo().getPromoCode()+" "+value.getInfo().getInDemand());

            }

            @Override
            public void onError(Throwable t) {
finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
finishLatch.countDown();
            }
        };

        StreamObserver<Order > requestObserver = asyncStub.clientSideStreamingCreateOrders(respObserver);
        try
        {
            for (int i=0;i<7;i++) {
                Thread.sleep(2000);
                requestObserver.onNext(cord);
                if(finishLatch.getCount()==0)
                    return;
            }
        }
        catch (RuntimeException e)
        {

        }
        requestObserver.onCompleted();
        finishLatch.await(5, TimeUnit.MINUTES);

        // Bi-directional streaming
        Order biord = Order.newBuilder().setProductCode("VCX34GJ").setProductName("AIR6449").setProductQty(8700)
                .setPrice(6789.34).setProductInfo(info.newBuilder().setProductType("radios").setProductBatch("V1").build()).build();
        final CountDownLatch bidi_latch = new CountDownLatch(1);
        StreamObserver<ProcessOrder> bidi_resp = new StreamObserver<ProcessOrder>() {
            @Override
            public void onNext(ProcessOrder value) {
                System.out.println("streaming bidi server response in chunks");
                System.out.println("bidi resp status:" +" "+value.getStatus());
                System.out.println("bidi resp inv_qty:"+ " "+value.getInvQty());
                System.out.println("bidi resp info_demand:"+" "+value.getInfo().getInDemand());
                System.out.println("bidi resp info_promo_code:"+ " "+value.getInfo().getPromoCode());
            }

            @Override
            public void onError(Throwable t) {
                bidi_latch.countDown();


            }

            @Override
            public void onCompleted() {
bidi_latch.countDown();
            }
        };

        StreamObserver<Order> bidi_req = asyncStub.bidirectionalStreamingPoolOrders(bidi_resp);
        try
        {
            for(int i=0;i<16;i++)
            {
                Thread.sleep(2000);
                bidi_req.onNext(biord);
                if(bidi_latch.getCount()==0)
                    return;
            }
        }
        catch (RuntimeException e)
        {

        }
        bidi_req.onCompleted();
        bidi_latch.await(5,TimeUnit.MINUTES);
        channel.shutdown();
    }

}

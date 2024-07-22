package com.baeldung.grpc.order;

import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Collection;

public class OrderServiceImpl extends orderServiceProviderGrpc.orderServiceProviderImplBase{

    @Override
    public void serverSideStreamingGetOrders(Order request, StreamObserver<ProcessOrder> responseObserver) {

        Collection<ProcessOrder> pOrders = new ArrayList<>();
        String input_code = request.getProductCode();
        double input_price = request.getPrice();
        String input_name = request.getProductName();
        int input_qty = request.getProductQty();
        info input_info = request.getProductInfo();
        String input_batch = input_info.getProductBatch();
        String input_type = input_info.getProductType();
        for (int loop=0;loop<10;loop++) {
            ProcessOrder pOrder = ProcessOrder.newBuilder().setStatus("true")
                    .setInvQty(loop)
                    .setInfo(pinfo.newBuilder()
                            .setPromoCode("xx"+loop)
                            .setInDemand(false).build())
                    .build();
            responseObserver.onNext(pOrder);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Order> clientSideStreamingCreateOrders(StreamObserver<ProcessOrder> responseObserver) {
        return new StreamObserver<Order>() {

            int loop_count=0;
            boolean in_demand;
            @Override
            public void onNext(Order value) {
                String input_code = value.getProductCode();
                double input_price = value.getPrice();
                String input_name = value.getProductName();
                int input_qty = value.getProductQty();
                info input_info = value.getProductInfo();
                String input_batch = input_info.getProductBatch();
                String input_type = input_info.getProductType();
                loop_count++;
                if(input_qty>2000)
                    in_demand = true;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                ProcessOrder pOrder = ProcessOrder.newBuilder().setInvQty(loop_count)
                        .setInfo(pinfo.newBuilder().setPromoCode("yy"+loop_count).setInDemand(in_demand).build()).build();
                responseObserver.onNext(pOrder);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Order> bidirectionalStreamingPoolOrders(StreamObserver<ProcessOrder> responseObserver) {
        return new StreamObserver<Order>() {
            int loop_count=0;
            boolean in_demand;
            @Override
            public void onNext(Order value) {
                String input_code = value.getProductCode();
                double input_price = value.getPrice();
                String input_name = value.getProductName();
                int input_qty = value.getProductQty();
                info input_info = value.getProductInfo();
                String input_batch = input_info.getProductBatch();
                String input_type = input_info.getProductType();
                loop_count++;
                if(input_qty>20000)
                    in_demand = true;
                ProcessOrder pOrder = ProcessOrder.newBuilder().setInvQty(loop_count)
                        .setInfo(pinfo.newBuilder().setPromoCode("yy"+loop_count).setInDemand(in_demand).build()).build();
                responseObserver.onNext(pOrder);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
             responseObserver.onCompleted();
            }
        };
    }
}

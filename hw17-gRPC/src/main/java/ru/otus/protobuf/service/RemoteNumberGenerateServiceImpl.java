package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.RemoteNumberGenerateServiceGrpc;
import ru.otus.protobuf.ResponseMessage;
import ru.otus.protobuf.StartMessage;

public class RemoteNumberGenerateServiceImpl
        extends RemoteNumberGenerateServiceGrpc.RemoteNumberGenerateServiceImplBase {

    @Override
    public void generateNumber(StartMessage request, StreamObserver<ResponseMessage> responseObserver) {
        long firstValue = request.getFirstValue();
        long lastValue = request.getLastValue();
        for (long l = firstValue; l <= lastValue; l++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ResponseMessage responseMessage =
                    ResponseMessage.newBuilder().setValue(l).build();
            responseObserver.onNext(responseMessage);
        }
        responseObserver.onCompleted();
    }
}

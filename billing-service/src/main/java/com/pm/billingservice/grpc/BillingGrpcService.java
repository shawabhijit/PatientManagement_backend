package com.pm.billingservice.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import com.pm.billingservice.DTO.BillingAccountGrpcResponse;
import com.pm.billingservice.DTO.BillingAccountRequest;
import com.pm.billingservice.service.BillingAccountService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class BillingGrpcService extends BillingServiceImplBase {

    private final BillingAccountService billingAccountService;

    private static final Logger log = LoggerFactory.getLogger(
            BillingGrpcService.class);

    //TODO: learn about StreamObserver , it is a very powerful concept in grpc
    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest ,
                                     StreamObserver<BillingResponse> responseObserver) {
        log.info("createBillingAccount request received {}", billingRequest.toString());

        BillingAccountRequest request = BillingAccountRequest.builder()
                .patientId(UUID.fromString(billingRequest.getPatientId()))
                .patientEmail(billingRequest.getEmail())
                .patientName(billingRequest.getName())
                .build();
        BillingAccountGrpcResponse billingAccountGrpcResponse = billingAccountService.createBillingAccount(request);

        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId(billingAccountGrpcResponse.getId().toString())
                .setStatus(billingAccountGrpcResponse.getStatus().toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

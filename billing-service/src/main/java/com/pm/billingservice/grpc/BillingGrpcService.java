package com.pm.billingservice.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import billing.CloseResponse;
import com.pm.billingservice.DTO.BillingAccountGrpcResponse;
import com.pm.billingservice.DTO.BillingAccountRequest;
import com.pm.billingservice.DTO.BillingAccountResponse;
import com.pm.billingservice.DTO.BillingAccountUpdateRequest;
import com.pm.billingservice.model.enums.BillingStatus;
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

    public void updateBillingAccount(billing.UpdateRequest updateRequest,
                                     StreamObserver<BillingResponse> responseObserver) {
        log.info("updateBillingAccount request received {}", updateRequest.getPatientId());
        BillingAccountUpdateRequest request = BillingAccountUpdateRequest.builder()
                .patientEmail(updateRequest.getEmail())
                .patientName(updateRequest.getName())
                .status(BillingStatus.valueOf(updateRequest.getStatus()))
                .build();

        BillingAccountGrpcResponse billingAccountGrpcResponse = billingAccountService.updateBillingAccount(UUID.fromString(updateRequest.getPatientId()),request);

        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId(billingAccountGrpcResponse.getId().toString())
                .setStatus(billingAccountGrpcResponse.getStatus().toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void closeBillingAccount(billing.CloseRequest closeRequest,
                                    StreamObserver<CloseResponse> responseObserver) {
        log.info("closeBillingAccount request received {}", closeRequest.getPatientId());
        CloseResponse response = billingAccountService.deleteBillingAccount(UUID.fromString(closeRequest.getPatientId()));
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

package com.patientservice.grpc;

import billing.*;
import billing.BillingServiceGrpc.BillingServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {

    private final BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort
    ) {
        log.info("Connecting to Billing Service GRPC service at {}:{}", serverAddress,serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext().build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(
            String patientId,
            String name,
            String email
    ) {
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email).build();

        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Received response from Billing Service via GRPC: {}", response);
        return response;
    }

    public BillingResponse updateBillingAccount(String patientId , String name , String email, String status) {
        UpdateRequest request = UpdateRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .setStatus(status).build();
        BillingResponse response = blockingStub.updateBillingAccount(request);
        log.info("Received response from Billing Service to update account via GRPC: {}", response);
        return response;
    }

    public CloseResponse closeBillingAccount(String patientId) {
        CloseRequest request = CloseRequest.newBuilder().setPatientId(patientId).build();
        CloseResponse response = blockingStub.closeBillingAccount(request);
        log.info("Received response from Billing Service to close account via GRPC: {}", response);
        return response;
    }
}

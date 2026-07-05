package com.pm.billingservice.model;

import com.pm.billingservice.model.enums.BillingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "billing_accounts" , indexes = {
        @Index(name = "idx_patient_id", columnList = "patientId")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    @NotNull
    @Column(unique = true)
    public UUID patientId;

    @NotNull
    private String patientName;

    @NotNull
    @Email
    @Column(unique = true)
    private String patientEmail;

    @Column(nullable = false , precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal outstandingBalance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BillingStatus status;

    @CreationTimestamp
    private LocalDate createdAt;
    @UpdateTimestamp
    private LocalDate updatedAt;

}

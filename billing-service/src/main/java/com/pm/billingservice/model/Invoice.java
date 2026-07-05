package com.pm.billingservice.model;

import com.pm.billingservice.model.enums.InvoiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "invoices",
        indexes = {
            @Index(name = "idx_invoice_number" , columnList = "invoiceNumber"),
            @Index(name = "idx_billing_account", columnList = "billingAccountId"),
            @Index(name = "idx_invoice_status", columnList = "status")
        }

)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "billingAccountId", nullable = false)
    private BillingAccount billingAccount;

    @Column(unique = true)
    @NotNull
    private String invoiceNumber;
    @NotNull
    private BigDecimal consultationFee;
    @NotNull
    private BigDecimal medicineFee;
    @NotNull
    private BigDecimal labFee;
    @NotNull
    private BigDecimal discount;
    @NotNull
    private BigDecimal tax;
    @NotNull
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private InvoiceStatus status;
    @NotNull
    private LocalDate issueDate;
    @NotNull
    private LocalDate dueDate;
}

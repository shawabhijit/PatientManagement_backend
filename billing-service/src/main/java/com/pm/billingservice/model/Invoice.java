package com.pm.billingservice.model;

import com.pm.billingservice.model.enums.InvoiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal consultationFee;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal medicineFee;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal labFee;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal discount;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal tax;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private InvoiceStatus status;

    @NotNull
    @CreationTimestamp
    private LocalDateTime issueDate;

    @NotNull
    private LocalDateTime dueDate;
}

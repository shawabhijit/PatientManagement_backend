package com.pm.billingservice.model;

import com.pm.billingservice.model.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "invoiceId", nullable = false)
    private Invoice invoice;

    @Column(nullable = false , precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentMethod method;

    @NotNull
    @CreationTimestamp
    private LocalDate paymentDate;
}

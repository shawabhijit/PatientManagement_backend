package com.pm.billingservice.repository;

import com.pm.billingservice.model.Invoice;
import com.pm.billingservice.model.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice , UUID> {
    Optional<Invoice> findByInvoiceNumber(String invoiceId);
    List<Invoice> findByStatus(InvoiceStatus status);

    @Query(value = "SELECT nextval('invoice_number_seq')", nativeQuery = true)
    Long getNextInvoiceSequence();
}

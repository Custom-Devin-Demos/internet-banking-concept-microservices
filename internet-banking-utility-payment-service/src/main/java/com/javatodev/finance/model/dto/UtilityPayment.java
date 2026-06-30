package com.javatodev.finance.model.dto;

import com.javatodev.finance.model.TransactionStatus;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

import lombok.Data;

@Schema(description = "Utility payment transaction details")
@Data
public class UtilityPayment extends AuditAware {

    @Schema(description = "Identifier of the utility provider (biller)", example = "1001")
    private Long providerId;

    @Schema(description = "Paid amount", example = "150.00")
    private BigDecimal amount;

    @Schema(description = "Biller reference / invoice number", example = "INV-2024-0001")
    private String referenceNumber;

    @Schema(description = "Source bank account number used for the payment", example = "100200300")
    private String account;

    @Schema(description = "Current status of the utility payment", example = "SUCCESS")
    private TransactionStatus status;
}

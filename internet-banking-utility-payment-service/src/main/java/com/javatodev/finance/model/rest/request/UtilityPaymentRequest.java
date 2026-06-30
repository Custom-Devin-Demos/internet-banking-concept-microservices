package com.javatodev.finance.model.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

import lombok.Data;

@Schema(description = "Request payload for processing a utility payment")
@Data
public class UtilityPaymentRequest {

    @Schema(description = "Identifier of the utility provider (biller)", example = "1001")
    private Long providerId;

    @Schema(description = "Amount to be paid", example = "150.00")
    private BigDecimal amount;

    @Schema(description = "Biller reference / invoice number", example = "INV-2024-0001")
    private String referenceNumber;

    @Schema(description = "Source bank account number used for the payment", example = "100200300")
    private String account;
}

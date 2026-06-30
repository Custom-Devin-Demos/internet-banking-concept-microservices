package com.javatodev.finance.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

import lombok.Data;

@Data
@Schema(description = "Request payload to pay a utility bill from a bank account")
public class UtilityPaymentRequest {

    @Schema(description = "Identifier of the utility provider", example = "1")
    private Long providerId;

    @Schema(description = "Amount to pay", example = "75.50")
    private BigDecimal amount;

    @Schema(description = "Biller reference number for the payment", example = "REF-99812")
    private String referenceNumber;

    @Schema(description = "Bank account number money is debited from", example = "1100000000")
    private String account;

}

package com.javatodev.finance.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload to transfer funds between two bank accounts")
public class FundTransferRequest {

    @Schema(description = "Account number money is debited from", example = "1100000000")
    private String fromAccount;

    @Schema(description = "Account number money is credited to", example = "1100000001")
    private String toAccount;

    @Schema(description = "Amount to transfer", example = "150.00")
    private BigDecimal amount;
}

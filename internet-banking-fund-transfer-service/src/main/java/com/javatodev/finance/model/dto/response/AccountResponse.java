package com.javatodev.finance.model.dto.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Bank account details returned by the core-banking service")
public class AccountResponse {

    @Schema(description = "Account number", example = "1000000001")
    private Long number;

    @Schema(description = "Actual balance including unsettled amounts", example = "5000.00")
    private BigDecimal actualBalance;

    @Schema(description = "Unique identifier of the account", example = "10")
    private Long id;

    @Schema(description = "Account type", example = "SAVING")
    private String type;

    @Schema(description = "Account status", example = "ACTIVE")
    private String status;

    @Schema(description = "Balance available for transactions", example = "4500.00")
    private BigDecimal availableBalance;
}

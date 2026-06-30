package com.javatodev.finance.model.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request payload to initiate a fund transfer between two accounts")
public class FundTransferRequest {

    @Schema(description = "Account number money is debited from", example = "1000000001")
    private String fromAccount;

    @Schema(description = "Account number money is credited to", example = "1000000002")
    private String toAccount;

    @Schema(description = "Amount to transfer", example = "150.00")
    private BigDecimal amount;

    @Schema(description = "Authorization identifier of the user initiating the transfer", example = "auth-123")
    private String authID;
}

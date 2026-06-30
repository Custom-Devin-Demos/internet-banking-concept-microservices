package com.javatodev.finance.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Result of a processed fund transfer")
public class FundTransferResponse {

    @Schema(description = "Human readable result message", example = "Fund Transfer Successfully Completed")
    private String message;

    @Schema(description = "Reference identifier of the completed core-banking transaction", example = "TXN-000123")
    private String transactionId;
}

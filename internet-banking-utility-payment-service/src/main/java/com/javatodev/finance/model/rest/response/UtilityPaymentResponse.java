package com.javatodev.finance.model.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;

@Schema(description = "Response returned after processing a utility payment")
@Data
@Builder
public class UtilityPaymentResponse {

    @Schema(description = "Human readable result message", example = "Utility Payment Successfully Processed")
    private String message;

    @Schema(description = "Identifier of the created core-banking transaction", example = "TXN-123456")
    private String transactionId;
}

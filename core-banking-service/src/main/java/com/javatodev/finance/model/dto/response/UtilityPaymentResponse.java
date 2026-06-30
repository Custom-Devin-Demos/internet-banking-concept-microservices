package com.javatodev.finance.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Result of a utility payment request")
public class UtilityPaymentResponse {

    @Schema(description = "Human readable result message", example = "Utility payment successfully completed")
    private String message;

    @Schema(description = "Unique identifier of the completed transaction", example = "9f1c2d3e-4b5a-6789-0123-456789abcdef")
    private String transactionId;
}

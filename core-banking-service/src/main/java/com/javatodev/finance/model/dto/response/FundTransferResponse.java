package com.javatodev.finance.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(description = "Result of a fund transfer request")
public class FundTransferResponse {

    @Schema(description = "Human readable result message", example = "Transaction successfully completed")
    private String message;

    @Schema(description = "Unique identifier of the completed transaction", example = "9f1c2d3e-4b5a-6789-0123-456789abcdef")
    private String transactionId;

}

package com.javatodev.finance.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(description = "Utility provider account used for bill payments")
public class UtilityAccount {

    @Schema(description = "Internal identifier of the utility account", example = "1")
    private Long id;

    @Schema(description = "Utility account number", example = "9900000000")
    private String number;

    @Schema(description = "Name of the utility provider", example = "Ceylon Electricity Board")
    private String providerName;
}

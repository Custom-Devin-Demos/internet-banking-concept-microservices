package com.javatodev.finance.model.dto;

import com.javatodev.finance.model.AccountStatus;
import com.javatodev.finance.model.AccountType;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

import lombok.Data;

@Data
@Schema(description = "Bank account held in the core banking system of record")
public class BankAccount {

    @Schema(description = "Internal identifier of the account", example = "1")
    private Long id;

    @Schema(description = "Unique account number", example = "1100000000")
    private String number;

    @Schema(description = "Type of the account")
    private AccountType type;

    @Schema(description = "Current status of the account")
    private AccountStatus status;

    @Schema(description = "Balance available for withdrawal", example = "950.00")
    private BigDecimal availableBalance;

    @Schema(description = "Actual ledger balance", example = "1000.00")
    private BigDecimal actualBalance;

    @Schema(description = "Owner of the account")
    private User user;

}

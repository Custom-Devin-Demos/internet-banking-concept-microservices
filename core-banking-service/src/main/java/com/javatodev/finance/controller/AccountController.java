package com.javatodev.finance.controller;

import com.javatodev.finance.exception.ErrorResponse;
import com.javatodev.finance.model.dto.BankAccount;
import com.javatodev.finance.model.dto.UtilityAccount;
import com.javatodev.finance.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Account Controller", description = "APIs for managing accounts")
@RestController
@RequestMapping(value = "/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Get Bank Account by Account Number", description = "Retrieve bank account details by account number")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bank account found",
            content = @Content(schema = @Schema(implementation = BankAccount.class))),
        @ApiResponse(responseCode = "400", description = "Bank account not found for the given account number",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/bank-account/{account_number}")
    public ResponseEntity<BankAccount> getBankAccount(
        @Parameter(description = "Unique bank account number", example = "1100000000")
        @PathVariable("account_number") String accountNumber) {
        log.info("Reading account by ID {}", accountNumber);
        return ResponseEntity.ok(accountService.readBankAccount(accountNumber));
    }

    @Operation(summary = "Get Utility Account by Account Name", description = "Retrieve utility account details by account name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utility account found",
            content = @Content(schema = @Schema(implementation = UtilityAccount.class))),
        @ApiResponse(responseCode = "400", description = "Utility account not found for the given provider name",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/util-account/{account_name}")
    public ResponseEntity<UtilityAccount> getUtilityAccount(
        @Parameter(description = "Utility provider name", example = "Ceylon Electricity Board")
        @PathVariable("account_name") String providerName) {
        log.info("Reading utitlity account by ID {}", providerName);
        return ResponseEntity.ok(accountService.readUtilityAccount(providerName));
    }

}

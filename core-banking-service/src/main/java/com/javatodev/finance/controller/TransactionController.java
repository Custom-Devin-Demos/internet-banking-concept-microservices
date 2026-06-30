package com.javatodev.finance.controller;

import com.javatodev.finance.exception.ErrorResponse;
import com.javatodev.finance.model.dto.request.FundTransferRequest;
import com.javatodev.finance.model.dto.request.UtilityPaymentRequest;
import com.javatodev.finance.model.dto.response.FundTransferResponse;
import com.javatodev.finance.model.dto.response.UtilityPaymentResponse;
import com.javatodev.finance.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Transaction Controller", description = "APIs for managing transactions")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Fund Transfer", description = "Process a fund transfer request")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fund transfer completed",
            content = @Content(schema = @Schema(implementation = FundTransferResponse.class))),
        @ApiResponse(responseCode = "400", description = "Account not found or insufficient funds",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/fund-transfer")
    public ResponseEntity<FundTransferResponse> fundTransfer(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fund transfer details", required = true)
        @RequestBody FundTransferRequest fundTransferRequest) {

        log.info("Fund transfer initiated in core bank from {}", fundTransferRequest.toString());
        return ResponseEntity.ok(transactionService.fundTransfer(fundTransferRequest));

    }

    @Operation(summary = "Utility Payment", description = "Process a utility payment request")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utility payment completed",
            content = @Content(schema = @Schema(implementation = UtilityPaymentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Account not found or insufficient funds",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/util-payment")
    public ResponseEntity<UtilityPaymentResponse> utilPayment(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Utility payment details", required = true)
        @RequestBody UtilityPaymentRequest utilityPaymentRequest) {

        log.info("Utility Payment initiated in core bank from {}", utilityPaymentRequest.toString());
        return ResponseEntity.ok(transactionService.utilPayment(utilityPaymentRequest));

    }

}

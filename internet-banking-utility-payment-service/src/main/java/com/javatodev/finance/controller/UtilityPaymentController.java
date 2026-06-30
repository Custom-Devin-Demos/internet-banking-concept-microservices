package com.javatodev.finance.controller;

import com.javatodev.finance.model.dto.UtilityPayment;
import com.javatodev.finance.model.rest.request.UtilityPaymentRequest;
import com.javatodev.finance.model.rest.response.UtilityPaymentResponse;
import com.javatodev.finance.service.UtilityPaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Utility Payment API", description = "API for processing utility payments")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/utility-payment")
public class UtilityPaymentController {

    private final UtilityPaymentService utilityPaymentService;

    @Operation(summary = "Read Utility Payments", description = "Retrieve a paginated list of utility payments")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utility payments retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request",
            content = @Content(schema = @Schema(implementation = com.javatodev.finance.exception.ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<UtilityPayment>> readPayments(
        @Parameter(description = "Pagination and sorting parameters (page, size, sort)") Pageable pageable) {
        log.info("Reading utility payments with pageable {}", pageable);
        return ResponseEntity.ok(utilityPaymentService.readPayments(pageable));
    }

    @Operation(summary = "Process Utility Payment", description = "Process a utility payment request")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utility payment processed successfully"),
        @ApiResponse(responseCode = "400", description = "Utility payment could not be processed",
            content = @Content(schema = @Schema(implementation = com.javatodev.finance.exception.ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<UtilityPaymentResponse> processPayment(
        @Parameter(description = "Utility payment details to process", required = true)
        @RequestBody UtilityPaymentRequest paymentRequest) {
        log.info("Processing utility payment {}", paymentRequest);
        return ResponseEntity.ok(utilityPaymentService.utilPayment(paymentRequest));
    }

}

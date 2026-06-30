package com.javatodev.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatodev.finance.exception.GlobalErrorCode;
import com.javatodev.finance.exception.InsufficientFundsException;
import com.javatodev.finance.model.dto.request.FundTransferRequest;
import com.javatodev.finance.model.dto.request.UtilityPaymentRequest;
import com.javatodev.finance.model.dto.response.FundTransferResponse;
import com.javatodev.finance.model.dto.response.UtilityPaymentResponse;
import com.javatodev.finance.service.TransactionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Test
    void fundTransfer_success() throws Exception {
        FundTransferRequest request = new FundTransferRequest("A1", "A2", BigDecimal.valueOf(100));
        FundTransferResponse response = FundTransferResponse.builder()
            .message("Transaction successfully completed")
            .transactionId("txn-1")
            .build();
        when(transactionService.fundTransfer(any(FundTransferRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/transaction/fund-transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.transactionId").value("txn-1"))
            .andExpect(jsonPath("$.message").value("Transaction successfully completed"));
    }

    @Test
    void fundTransfer_insufficientFunds_returnsBadRequest() throws Exception {
        FundTransferRequest request = new FundTransferRequest("A1", "A2", BigDecimal.valueOf(100));
        when(transactionService.fundTransfer(any(FundTransferRequest.class)))
            .thenThrow(new InsufficientFundsException("Insufficient funds in the account A1", GlobalErrorCode.INSUFFICIENT_FUNDS));

        mockMvc.perform(post("/api/v1/transaction/fund-transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").exists())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void utilPayment_success() throws Exception {
        UtilityPaymentRequest request = new UtilityPaymentRequest();
        request.setAccount("A1");
        request.setProviderId(1L);
        request.setAmount(BigDecimal.valueOf(50));
        request.setReferenceNumber("REF123");
        UtilityPaymentResponse response = UtilityPaymentResponse.builder()
            .message("Utility payment successfully completed")
            .transactionId("txn-2")
            .build();
        when(transactionService.utilPayment(any(UtilityPaymentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/transaction/util-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.transactionId").value("txn-2"))
            .andExpect(jsonPath("$.message").value("Utility payment successfully completed"));
    }
}

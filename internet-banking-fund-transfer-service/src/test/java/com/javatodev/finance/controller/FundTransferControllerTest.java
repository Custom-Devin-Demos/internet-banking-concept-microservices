package com.javatodev.finance.controller;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatodev.finance.model.dto.FundTransfer;
import com.javatodev.finance.model.dto.request.FundTransferRequest;
import com.javatodev.finance.model.dto.response.FundTransferResponse;
import com.javatodev.finance.service.FundTransferService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FundTransferController.class)
class FundTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FundTransferService fundTransferService;

    @Test
    void sendFundTransfer_returnsResponse() throws Exception {
        FundTransferRequest request = new FundTransferRequest();
        request.setFromAccount("1000000001");
        request.setToAccount("1000000002");
        request.setAmount(new BigDecimal("150.00"));
        request.setAuthID("auth-123");

        FundTransferResponse response = new FundTransferResponse();
        response.setMessage("Fund Transfer Successfully Completed");
        response.setTransactionId("TXN-000123");
        when(fundTransferService.fundTransfer(any(FundTransferRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.transactionId").value("TXN-000123"))
            .andExpect(jsonPath("$.message").value("Fund Transfer Successfully Completed"));
    }

    @Test
    void readFundTransfers_returnsList() throws Exception {
        FundTransfer transfer = new FundTransfer();
        transfer.setId(1L);
        transfer.setFromAccount("1000000001");
        transfer.setToAccount("1000000002");
        transfer.setAmount(new BigDecimal("150.00"));
        transfer.setTransactionReference("TXN-000123");
        transfer.setStatus("SUCCESS");
        when(fundTransferService.readAllTransfers(any())).thenReturn(List.of(transfer));

        mockMvc.perform(get("/api/v1/transfer"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].fromAccount").value("1000000001"))
            .andExpect(jsonPath("$[0].status").value("SUCCESS"));
    }
}

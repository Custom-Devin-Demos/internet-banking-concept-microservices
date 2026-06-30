package com.javatodev.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatodev.finance.model.TransactionStatus;
import com.javatodev.finance.model.dto.UtilityPayment;
import com.javatodev.finance.model.rest.request.UtilityPaymentRequest;
import com.javatodev.finance.model.rest.response.UtilityPaymentResponse;
import com.javatodev.finance.service.UtilityPaymentService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UtilityPaymentController.class)
class UtilityPaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UtilityPaymentService utilityPaymentService;

    @Test
    void readPayments_returnsOkWithList() throws Exception {
        UtilityPayment payment = new UtilityPayment();
        payment.setProviderId(1001L);
        payment.setAmount(new BigDecimal("150.00"));
        payment.setReferenceNumber("INV-2024-0001");
        payment.setAccount("100200300");
        payment.setStatus(TransactionStatus.SUCCESS);
        when(utilityPaymentService.readPayments(any(Pageable.class))).thenReturn(List.of(payment));

        mockMvc.perform(get("/api/v1/utility-payment"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].providerId").value(1001))
            .andExpect(jsonPath("$[0].referenceNumber").value("INV-2024-0001"))
            .andExpect(jsonPath("$[0].status").value("SUCCESS"));
    }

    @Test
    void processPayment_returnsOkWithResponse() throws Exception {
        UtilityPaymentRequest request = new UtilityPaymentRequest();
        request.setProviderId(1001L);
        request.setAmount(new BigDecimal("150.00"));
        request.setReferenceNumber("INV-2024-0001");
        request.setAccount("100200300");

        when(utilityPaymentService.utilPayment(any(UtilityPaymentRequest.class))).thenReturn(
            UtilityPaymentResponse.builder()
                .message("Utility Payment Successfully Processed")
                .transactionId("TXN-123456")
                .build());

        mockMvc.perform(post("/api/v1/utility-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Utility Payment Successfully Processed"))
            .andExpect(jsonPath("$.transactionId").value("TXN-123456"));
    }
}

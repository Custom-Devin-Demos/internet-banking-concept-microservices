package com.javatodev.finance.service;

import com.javatodev.finance.model.TransactionStatus;
import com.javatodev.finance.model.dto.UtilityPayment;
import com.javatodev.finance.model.entity.UtilityPaymentEntity;
import com.javatodev.finance.model.rest.request.UtilityPaymentRequest;
import com.javatodev.finance.model.rest.response.UtilityPaymentResponse;
import com.javatodev.finance.repository.UtilityPaymentRepository;
import com.javatodev.finance.service.rest.BankingCoreRestClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilityPaymentServiceTest {

    @Mock
    private UtilityPaymentRepository utilityPaymentRepository;

    @Mock
    private BankingCoreRestClient bankingCoreRestClient;

    @InjectMocks
    private UtilityPaymentService utilityPaymentService;

    private UtilityPaymentRequest sampleRequest() {
        UtilityPaymentRequest request = new UtilityPaymentRequest();
        request.setProviderId(1001L);
        request.setAmount(new BigDecimal("150.00"));
        request.setReferenceNumber("INV-2024-0001");
        request.setAccount("100200300");
        return request;
    }

    @Test
    void utilPayment_persistsAndReturnsSuccessResponse() {
        UtilityPaymentRequest request = sampleRequest();

        UtilityPaymentEntity savedEntity = new UtilityPaymentEntity();
        savedEntity.setId(1L);
        when(utilityPaymentRepository.save(any(UtilityPaymentEntity.class))).thenReturn(savedEntity);
        when(bankingCoreRestClient.utilityPayment(request)).thenReturn(
            UtilityPaymentResponse.builder().transactionId("TXN-123456").message("ok").build());

        UtilityPaymentResponse response = utilityPaymentService.utilPayment(request);

        assertNotNull(response);
        assertEquals("Utility Payment Successfully Processed", response.getMessage());
        assertEquals("TXN-123456", response.getTransactionId());

        ArgumentCaptor<UtilityPaymentEntity> captor = ArgumentCaptor.forClass(UtilityPaymentEntity.class);
        verify(utilityPaymentRepository, times(2)).save(captor.capture());
        List<UtilityPaymentEntity> persisted = captor.getAllValues();
        assertEquals(TransactionStatus.PROCESSING, persisted.get(0).getStatus());
        assertEquals(TransactionStatus.SUCCESS, persisted.get(1).getStatus());
        assertEquals("TXN-123456", persisted.get(1).getTransactionId());
        verify(bankingCoreRestClient, times(1)).utilityPayment(request);
    }

    @Test
    void readPayments_mapsEntitiesToDtos() {
        UtilityPaymentEntity entity = new UtilityPaymentEntity();
        entity.setProviderId(1001L);
        entity.setAmount(new BigDecimal("150.00"));
        entity.setReferenceNumber("INV-2024-0001");
        entity.setAccount("100200300");
        entity.setStatus(TransactionStatus.SUCCESS);

        Pageable pageable = PageRequest.of(0, 10);
        Page<UtilityPaymentEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(utilityPaymentRepository.findAll(pageable)).thenReturn(page);

        List<UtilityPayment> result = utilityPaymentService.readPayments(pageable);

        assertEquals(1, result.size());
        UtilityPayment dto = result.get(0);
        assertEquals(1001L, dto.getProviderId());
        assertEquals(new BigDecimal("150.00"), dto.getAmount());
        assertEquals("INV-2024-0001", dto.getReferenceNumber());
        assertEquals("100200300", dto.getAccount());
        assertEquals(TransactionStatus.SUCCESS, dto.getStatus());
        verify(utilityPaymentRepository, times(1)).findAll(pageable);
    }
}

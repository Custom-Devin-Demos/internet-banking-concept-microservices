package com.javatodev.finance.service;

import java.math.BigDecimal;
import java.util.List;

import com.javatodev.finance.model.TransactionStatus;
import com.javatodev.finance.model.dto.FundTransfer;
import com.javatodev.finance.model.dto.request.FundTransferRequest;
import com.javatodev.finance.model.dto.response.FundTransferResponse;
import com.javatodev.finance.model.entity.FundTransferEntity;
import com.javatodev.finance.model.repository.FundTransferRepository;
import com.javatodev.finance.service.rest.client.BankingCoreFeignClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundTransferServiceTest {

    @Mock
    private FundTransferRepository fundTransferRepository;

    @Mock
    private BankingCoreFeignClient bankingCoreFeignClient;

    @InjectMocks
    private FundTransferService fundTransferService;

    private FundTransferRequest sampleRequest() {
        FundTransferRequest request = new FundTransferRequest();
        request.setFromAccount("1000000001");
        request.setToAccount("1000000002");
        request.setAmount(new BigDecimal("150.00"));
        request.setAuthID("auth-123");
        return request;
    }

    @Test
    void fundTransfer_persistsAndReturnsCompletedResponse() {
        FundTransferRequest request = sampleRequest();

        when(fundTransferRepository.save(any(FundTransferEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        FundTransferResponse coreResponse = new FundTransferResponse();
        coreResponse.setTransactionId("TXN-000123");
        when(bankingCoreFeignClient.fundTransfer(request)).thenReturn(coreResponse);

        FundTransferResponse result = fundTransferService.fundTransfer(request);

        assertNotNull(result);
        assertEquals("TXN-000123", result.getTransactionId());
        assertEquals("Fund Transfer Successfully Completed", result.getMessage());

        ArgumentCaptor<FundTransferEntity> captor = ArgumentCaptor.forClass(FundTransferEntity.class);
        verify(fundTransferRepository, times(2)).save(captor.capture());
        verify(bankingCoreFeignClient).fundTransfer(request);

        FundTransferEntity persisted = captor.getValue();
        assertEquals("1000000001", persisted.getFromAccount());
        assertEquals("1000000002", persisted.getToAccount());
        assertEquals(new BigDecimal("150.00"), persisted.getAmount());
        assertEquals("TXN-000123", persisted.getTransactionReference());
        assertEquals(TransactionStatus.SUCCESS, persisted.getStatus());
    }

    @Test
    void readAllTransfers_mapsEntitiesToDtos() {
        FundTransferEntity entity = new FundTransferEntity();
        entity.setId(1L);
        entity.setFromAccount("1000000001");
        entity.setToAccount("1000000002");
        entity.setAmount(new BigDecimal("75.50"));
        entity.setTransactionReference("TXN-1");
        entity.setStatus(TransactionStatus.SUCCESS);

        Pageable pageable = PageRequest.of(0, 10);
        when(fundTransferRepository.findAll(pageable))
            .thenReturn(new PageImpl<>(List.of(entity), pageable, 1));

        List<FundTransfer> result = fundTransferService.readAllTransfers(pageable);

        assertEquals(1, result.size());
        FundTransfer dto = result.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("1000000001", dto.getFromAccount());
        assertEquals("1000000002", dto.getToAccount());
        assertEquals(new BigDecimal("75.50"), dto.getAmount());
        assertEquals("TXN-1", dto.getTransactionReference());
    }
}

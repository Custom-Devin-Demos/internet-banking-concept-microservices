package com.javatodev.finance.model.repository;

import java.math.BigDecimal;

import com.javatodev.finance.model.TransactionStatus;
import com.javatodev.finance.model.entity.FundTransferEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class FundTransferRepositoryTest {

    @Autowired
    private FundTransferRepository fundTransferRepository;

    private FundTransferEntity newEntity(String reference) {
        FundTransferEntity entity = new FundTransferEntity();
        entity.setFromAccount("1000000001");
        entity.setToAccount("1000000002");
        entity.setAmount(new BigDecimal("150.00"));
        entity.setTransactionReference(reference);
        entity.setStatus(TransactionStatus.SUCCESS);
        return entity;
    }

    @Test
    void saveAndFindById() {
        FundTransferEntity saved = fundTransferRepository.save(newEntity("TXN-1"));

        assertNotNull(saved.getId());
        FundTransferEntity found = fundTransferRepository.findById(saved.getId()).orElseThrow();
        assertEquals("TXN-1", found.getTransactionReference());
        assertEquals(TransactionStatus.SUCCESS, found.getStatus());
        assertEquals(new BigDecimal("150.00"), found.getAmount());
    }

    @Test
    void findAll_paginated() {
        fundTransferRepository.save(newEntity("TXN-1"));
        fundTransferRepository.save(newEntity("TXN-2"));

        Page<FundTransferEntity> page = fundTransferRepository.findAll(PageRequest.of(0, 1));

        assertEquals(1, page.getContent().size());
        assertEquals(2, page.getTotalElements());
        assertTrue(page.getTotalPages() >= 2);
    }
}

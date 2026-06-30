package com.javatodev.finance.repository;

import com.javatodev.finance.model.TransactionStatus;
import com.javatodev.finance.model.entity.UtilityPaymentEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class UtilityPaymentRepositoryTest {

    @Autowired
    private UtilityPaymentRepository utilityPaymentRepository;

    private UtilityPaymentEntity sampleEntity() {
        UtilityPaymentEntity entity = new UtilityPaymentEntity();
        entity.setProviderId(1001L);
        entity.setAmount(new BigDecimal("150.00"));
        entity.setReferenceNumber("INV-2024-0001");
        entity.setAccount("100200300");
        entity.setStatus(TransactionStatus.PROCESSING);
        return entity;
    }

    @Test
    void save_assignsIdAndPersistsFields() {
        UtilityPaymentEntity saved = utilityPaymentRepository.save(sampleEntity());

        assertNotNull(saved.getId());
        assertEquals(1001L, saved.getProviderId());
        assertEquals(TransactionStatus.PROCESSING, saved.getStatus());
    }

    @Test
    void findAll_returnsPagedResults() {
        utilityPaymentRepository.save(sampleEntity());
        utilityPaymentRepository.save(sampleEntity());

        Page<UtilityPaymentEntity> page = utilityPaymentRepository.findAll(PageRequest.of(0, 10));

        assertEquals(2, page.getTotalElements());
        assertTrue(page.getContent().size() >= 1);
    }
}

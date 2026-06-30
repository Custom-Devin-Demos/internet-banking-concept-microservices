package com.javatodev.finance.repository;

import com.javatodev.finance.model.entity.UtilityAccountEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false",
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false"
})
class UtilityAccountRepositoryTest {

    @Autowired
    private UtilityAccountRepository utilityAccountRepository;

    @Test
    void findByProviderName_returnsAccount() {
        UtilityAccountEntity entity = new UtilityAccountEntity();
        entity.setNumber("9900000000");
        entity.setProviderName("ProviderA");
        utilityAccountRepository.save(entity);

        Optional<UtilityAccountEntity> found = utilityAccountRepository.findByProviderName("ProviderA");
        assertTrue(found.isPresent());
        assertEquals("9900000000", found.get().getNumber());
    }

    @Test
    void findByProviderName_missing_returnsEmpty() {
        assertFalse(utilityAccountRepository.findByProviderName("does-not-exist").isPresent());
    }
}

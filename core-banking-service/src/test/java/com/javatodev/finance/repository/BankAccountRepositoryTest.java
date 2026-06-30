package com.javatodev.finance.repository;

import com.javatodev.finance.model.AccountStatus;
import com.javatodev.finance.model.AccountType;
import com.javatodev.finance.model.entity.BankAccountEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
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
class BankAccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    void findByNumber_returnsAccount() {
        BankAccountEntity entity = new BankAccountEntity();
        entity.setNumber("1100000000");
        entity.setType(AccountType.SAVINGS_ACCOUNT);
        entity.setStatus(AccountStatus.ACTIVE);
        entity.setActualBalance(BigDecimal.valueOf(1000));
        entity.setAvailableBalance(BigDecimal.valueOf(950));
        bankAccountRepository.save(entity);

        Optional<BankAccountEntity> found = bankAccountRepository.findByNumber("1100000000");
        assertTrue(found.isPresent());
        assertEquals(AccountType.SAVINGS_ACCOUNT, found.get().getType());
    }

    @Test
    void findByNumber_missing_returnsEmpty() {
        assertFalse(bankAccountRepository.findByNumber("does-not-exist").isPresent());
    }
}

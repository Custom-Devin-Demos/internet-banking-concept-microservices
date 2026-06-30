package com.javatodev.finance.repository;

import com.javatodev.finance.model.AccountStatus;
import com.javatodev.finance.model.AccountType;
import com.javatodev.finance.model.TransactionType;
import com.javatodev.finance.model.entity.BankAccountEntity;
import com.javatodev.finance.model.entity.TransactionEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false",
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false"
})
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void saveAndFind_transaction() {
        BankAccountEntity account = new BankAccountEntity();
        account.setNumber("1100000000");
        account.setType(AccountType.SAVINGS_ACCOUNT);
        account.setStatus(AccountStatus.ACTIVE);
        account.setActualBalance(BigDecimal.valueOf(1000));
        account.setAvailableBalance(BigDecimal.valueOf(1000));

        TransactionEntity transaction = TransactionEntity.builder()
            .transactionType(TransactionType.FUND_TRANSFER)
            .transactionId("txn-1")
            .referenceNumber("REF-1")
            .amount(BigDecimal.valueOf(-100))
            .account(account)
            .build();

        TransactionEntity saved = transactionRepository.save(transaction);
        assertNotNull(saved.getId());

        Optional<TransactionEntity> found = transactionRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(TransactionType.FUND_TRANSFER, found.get().getTransactionType());
        assertEquals("txn-1", found.get().getTransactionId());
        assertNotNull(found.get().getAccount());
    }
}

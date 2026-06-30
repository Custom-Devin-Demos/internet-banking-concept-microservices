package com.javatodev.finance.controller;

import com.javatodev.finance.exception.EntityNotFoundException;
import com.javatodev.finance.model.AccountStatus;
import com.javatodev.finance.model.AccountType;
import com.javatodev.finance.model.dto.BankAccount;
import com.javatodev.finance.model.dto.UtilityAccount;
import com.javatodev.finance.service.AccountService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    void getBankAccount_returnsAccount() throws Exception {
        BankAccount account = new BankAccount();
        account.setId(1L);
        account.setNumber("1100000000");
        account.setType(AccountType.SAVINGS_ACCOUNT);
        account.setStatus(AccountStatus.ACTIVE);
        account.setActualBalance(BigDecimal.valueOf(1000));
        account.setAvailableBalance(BigDecimal.valueOf(950));
        when(accountService.readBankAccount("1100000000")).thenReturn(account);

        mockMvc.perform(get("/api/v1/account/bank-account/{account_number}", "1100000000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.number").value("1100000000"))
            .andExpect(jsonPath("$.type").value("SAVINGS_ACCOUNT"));
    }

    @Test
    void getBankAccount_notFound_returnsBadRequest() throws Exception {
        when(accountService.readBankAccount("missing")).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/v1/account/bank-account/{account_number}", "missing"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getUtilityAccount_returnsAccount() throws Exception {
        UtilityAccount account = new UtilityAccount();
        account.setId(1L);
        account.setNumber("9900000000");
        account.setProviderName("ProviderA");
        when(accountService.readUtilityAccount("ProviderA")).thenReturn(account);

        mockMvc.perform(get("/api/v1/account/util-account/{account_name}", "ProviderA"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.providerName").value("ProviderA"));
    }
}

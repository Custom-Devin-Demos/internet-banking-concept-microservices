package com.javatodev.finance.service.rest.client;

import java.math.BigDecimal;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.javatodev.finance.model.dto.request.FundTransferRequest;
import com.javatodev.finance.model.dto.response.AccountResponse;
import com.javatodev.finance.model.dto.response.FundTransferResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import feign.Feign;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BankingCoreFeignClientWireMockTest {

    private WireMockServer wireMockServer;
    private BankingCoreFeignClient client;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();

        ObjectFactory<HttpMessageConverters> converters =
            () -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter());

        client = Feign.builder()
            .contract(new SpringMvcContract())
            .encoder(new SpringEncoder(converters))
            .decoder(new SpringDecoder(converters))
            .target(BankingCoreFeignClient.class, wireMockServer.baseUrl());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void fundTransfer_serializesRequestAndDeserializesResponse() {
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/transaction/fund-transfer"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"message\":\"OK\",\"transactionId\":\"TXN-000123\"}")));

        FundTransferRequest request = new FundTransferRequest();
        request.setFromAccount("1000000001");
        request.setToAccount("1000000002");
        request.setAmount(new BigDecimal("150.00"));
        request.setAuthID("auth-123");

        FundTransferResponse response = client.fundTransfer(request);

        assertEquals("TXN-000123", response.getTransactionId());
        assertEquals("OK", response.getMessage());

        wireMockServer.verify(postRequestedFor(urlEqualTo("/api/v1/transaction/fund-transfer"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withRequestBody(matchingJsonPath("$.fromAccount", equalTo("1000000001")))
            .withRequestBody(matchingJsonPath("$.toAccount", equalTo("1000000002")))
            .withRequestBody(matchingJsonPath("$.authID", equalTo("auth-123"))));
    }

    @Test
    void readAccount_buildsPathAndDeserializesResponse() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/account/bank-account/1000000001"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"number\":1000000001,\"id\":10,\"type\":\"SAVING\","
                    + "\"status\":\"ACTIVE\",\"actualBalance\":5000.00,\"availableBalance\":4500.00}")));

        AccountResponse account = client.readAccount("1000000001");

        assertEquals(1000000001L, account.getNumber());
        assertEquals(10L, account.getId());
        assertEquals("SAVING", account.getType());
        assertEquals("ACTIVE", account.getStatus());
        assertEquals(new BigDecimal("4500.00"), account.getAvailableBalance());

        wireMockServer.verify(getRequestedFor(urlEqualTo("/api/v1/account/bank-account/1000000001")));
    }
}

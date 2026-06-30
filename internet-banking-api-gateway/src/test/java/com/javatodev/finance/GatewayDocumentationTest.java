package com.javatodev.finance;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayDocumentationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private OpenAPI gatewayOpenAPI;

    @Test
    void gatewayOpenApiBeanIsConstructed() {
        assertThat(gatewayOpenAPI).isNotNull();
        assertThat(gatewayOpenAPI.getInfo().getTitle()).isEqualTo("Internet Banking API Gateway");
    }

    @Test
    void aggregatedApiDocsEndpointIsPermittedAndReachable() {
        webTestClient.get().uri("/v3/api-docs")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.openapi").exists();
    }

    @Test
    void swaggerUiConfigListsEveryDownstreamService() {
        webTestClient.get().uri("/v3/api-docs/swagger-config")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.urls[?(@.name == 'internet-banking-user-service')].url").isEqualTo("/user/v3/api-docs")
            .jsonPath("$.urls[?(@.name == 'internet-banking-fund-transfer-service')].url").isEqualTo("/fund-transfer/v3/api-docs")
            .jsonPath("$.urls[?(@.name == 'core-banking-service')].url").isEqualTo("/banking-core/v3/api-docs")
            .jsonPath("$.urls[?(@.name == 'internet-banking-utility-payment-service')].url").isEqualTo("/utility-payment/v3/api-docs");
    }

    @Test
    void swaggerUiIsPermittedAndRedirectsToTheUiResource() {
        webTestClient.get().uri("/swagger-ui.html")
            .exchange()
            .expectStatus().is3xxRedirection()
            .expectHeader().value("Location", location -> assertThat(location).contains("swagger-ui"));
    }

}

package com.javatodev.finance.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "keycloak";

    @Bean
    public OpenAPI coreBankingOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Core Banking Service API")
                .version("v1")
                .description("Core banking APIs that act as the mock system of record for bank accounts, "
                    + "utility accounts, users and ledger transactions within the internet banking platform.")
                .contact(new Contact().name("Internet Banking Platform").email("support@javatodev.com"))
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
            .servers(List.of(
                new Server().url("http://localhost:8092").description("Direct core-banking-service access"),
                new Server().url("http://localhost:8082/core-banking-service").description("Access via API gateway")))
            .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
                new SecurityScheme()
                    .name(SECURITY_SCHEME_NAME)
                    .type(SecurityScheme.Type.OPENIDCONNECT)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Keycloak issued OAuth2 / OpenID Connect bearer token")
                    .openIdConnectUrl("http://localhost:8080/realms/banking/.well-known/openid-configuration")))
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}

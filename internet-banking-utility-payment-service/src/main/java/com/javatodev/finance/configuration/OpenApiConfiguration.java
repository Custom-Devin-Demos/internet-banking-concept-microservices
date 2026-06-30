package com.javatodev.finance.configuration;

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
public class OpenApiConfiguration {

    private static final String SECURITY_SCHEME_NAME = "keycloak";

    @Bean
    public OpenAPI utilityPaymentServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Internet Banking - Utility Payment Service API")
                .version("v1")
                .description("REST API for processing utility payments to external billers and "
                    + "reading historical utility payment transactions.")
                .contact(new Contact()
                    .name("javatodev")
                    .url("https://javatodev.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0")))
            .servers(List.of(
                new Server().url("http://localhost:8082").description("API Gateway"),
                new Server().url("http://localhost:8092").description("Utility Payment Service (direct)")))
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                    .name(SECURITY_SCHEME_NAME)
                    .type(SecurityScheme.Type.OAUTH2)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Keycloak issued OAuth2 / OpenID Connect bearer token")
                    .openIdConnectUrl("http://localhost:8080/realms/javatodev/.well-known/openid-configuration")));
    }
}

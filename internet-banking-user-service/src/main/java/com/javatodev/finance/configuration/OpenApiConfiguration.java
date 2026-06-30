package com.javatodev.finance.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    private static final String SECURITY_SCHEME_NAME = "keycloak";

    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Internet Banking - User Service API")
                        .version("v1")
                        .description("APIs for managing bank users including registration, approval "
                                + "and retrieval. User identities are managed through Keycloak and "
                                + "validated against the core banking system.")
                        .contact(new Contact().name("Javatodev").url("https://javatodev.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("API Gateway"),
                        new Server().url("http://localhost:8083").description("User Service (direct access)")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, keycloakSecurityScheme()));
    }

    private SecurityScheme keycloakSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.OAUTH2)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .openIdConnectUrl("http://localhost:8080/auth/realms/javatodev-internet-banking/.well-known/openid-configuration")
                .description("Keycloak based OAuth2 / OpenID Connect authentication. Obtain a bearer "
                        + "access token from the realm and send it in the Authorization header.");
    }
}

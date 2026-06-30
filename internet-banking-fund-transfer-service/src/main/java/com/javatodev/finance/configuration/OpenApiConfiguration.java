package com.javatodev.finance.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfiguration {

    private static final String SECURITY_SCHEME_NAME = "keycloak";

    @Bean
    public OpenAPI fundTransferOpenAPI() {
        Server localServer = new Server()
            .url("http://localhost:8092")
            .description("Local fund transfer service");
        Server gatewayServer = new Server()
            .url("http://localhost:8082/internet-banking-fund-transfer-service")
            .description("API gateway routed access");

        return new OpenAPI()
            .info(new Info()
                .title("Internet Banking Fund Transfer Service API")
                .version("v1")
                .description("REST API for processing account-to-account fund transfers "
                    + "and retrieving fund transfer history."))
            .servers(List.of(localServer, gatewayServer))
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .description("Keycloak OAuth2 / OpenID Connect bearer authentication")
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .openIdConnectUrl(
                        "http://localhost:8080/realms/javatodev/.well-known/openid-configuration")));
    }
}

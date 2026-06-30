package com.javatodev.finance.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI gatewayOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Internet Banking API Gateway")
                .description("Single entry point aggregating the OpenAPI documentation of the internet banking microservices.")
                .version("v1"));
    }

}

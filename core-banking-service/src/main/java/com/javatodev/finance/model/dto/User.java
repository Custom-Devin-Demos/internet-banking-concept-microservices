package com.javatodev.finance.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import lombok.Data;

@Data
@Schema(description = "Banking customer and their owned accounts")
public class User {

    @Schema(description = "Internal identifier of the user", example = "1")
    private Long id;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "National identification number", example = "199512345678")
    private String identificationNumber;

    @Schema(description = "Bank accounts owned by the user")
    private List<BankAccount> bankAccounts;

}

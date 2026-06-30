package com.javatodev.finance.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "Bank user account details")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AuditAware {

    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Email address of the user, also used as the Keycloak username", example = "john.doe@example.com")
    private String email;

    @Schema(description = "National identification number used to look the user up in core banking", example = "199512345678")
    private String identification;

    @Schema(description = "Password used to create the Keycloak credential", example = "Str0ngP@ss", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Schema(description = "Identifier of the user in Keycloak", example = "b8c1f0e2-1234-4d5e-9a8b-1c2d3e4f5a6b", accessMode = Schema.AccessMode.READ_ONLY)
    private String authId;

    @Schema(description = "Current lifecycle status of the user")
    private Status status;
}

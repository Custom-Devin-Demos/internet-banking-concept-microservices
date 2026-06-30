package com.javatodev.finance.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Request payload for updating an existing user")
@Data
public class UserUpdateRequest {

    @Schema(description = "New lifecycle status to apply to the user. Setting it to APPROVED also "
            + "enables the user in Keycloak.", example = "APPROVED")
    private Status status;
}

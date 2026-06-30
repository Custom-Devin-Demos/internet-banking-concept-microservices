package com.javatodev.finance.controller;

import com.javatodev.finance.exception.ErrorResponse;
import com.javatodev.finance.model.dto.User;
import com.javatodev.finance.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "User Controller", description = "APIs for managing users")
@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;

    @Operation(summary = "Read User by Identification", description = "Retrieve a user's information by their identification")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "User not found for the given identification",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{identification}")
    public ResponseEntity<User> readUser(
        @Parameter(description = "National identification number of the user", example = "199512345678")
        @PathVariable("identification") String identification) {
        return ResponseEntity.ok(userService.readUser(identification));
    }

    @Operation(summary = "Read Users", description = "Retrieve a paginated list of users")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))))
    })
    @GetMapping
    public ResponseEntity<List<User>> readUsers(
        @Parameter(description = "Pagination and sorting parameters") Pageable pageable) {
        return ResponseEntity.ok(userService.readUsers(pageable));
    }

}

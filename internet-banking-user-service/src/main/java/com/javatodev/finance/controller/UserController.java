package com.javatodev.finance.controller;

import com.javatodev.finance.exception.ErrorResponse;
import com.javatodev.finance.model.dto.User;
import com.javatodev.finance.model.dto.UserUpdateRequest;
import com.javatodev.finance.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "User Controller", description = "APIs for managing bank users")
@RestController
@RequestMapping(value = "/api/v1/bank-users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register User", description = "Create a new user in the banking system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Email already registered, invalid email "
                    + "or no matching user found under the given identification",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/register")
    public ResponseEntity<User> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration details", required = true,
                    content = @Content(schema = @Schema(implementation = User.class)))
            @RequestBody User request) {
        log.info("Creating user with {}", request.toString());
        return ResponseEntity.ok(userService.createUser(request));
    }

    @Operation(summary = "Update User", description = "Update an existing user's information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "User not found for the given id",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping(value = "/update/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "Identifier of the user to update", required = true, example = "1")
            @PathVariable("id") Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Fields to update on the user", required = true,
                    content = @Content(schema = @Schema(implementation = UserUpdateRequest.class)))
            @RequestBody UserUpdateRequest userUpdateRequest) {
        log.info("Updating user with {}", userUpdateRequest.toString());
        return ResponseEntity.ok(userService.updateUser(userId, userUpdateRequest));
    }

    @Operation(summary = "Read Users", description = "Retrieve a paginated list of users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(array = @io.swagger.v3.oas.annotations.media.ArraySchema(
                    schema = @Schema(implementation = User.class))))
    @GetMapping
    public ResponseEntity<List<User>> readUsers(
            @Parameter(description = "Pagination and sorting parameters (page, size, sort)") Pageable pageable) {
        log.info("Reading all users from API");
        return ResponseEntity.ok(userService.readUsers(pageable));
    }

    @Operation(summary = "Read User by ID", description = "Retrieve a user's information by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "User not found for the given id",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> readUser(
            @Parameter(description = "Identifier of the user to retrieve", required = true, example = "1")
            @PathVariable("id") Long id) {
        log.info("Reading user by id {}", id);
        return ResponseEntity.ok(userService.readUser(id));
    }

}

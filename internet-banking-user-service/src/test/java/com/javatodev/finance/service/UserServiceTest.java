package com.javatodev.finance.service;

import com.javatodev.finance.exception.EntityNotFoundException;
import com.javatodev.finance.exception.InvalidBankingUserException;
import com.javatodev.finance.exception.InvalidEmailException;
import com.javatodev.finance.exception.UserAlreadyRegisteredException;
import com.javatodev.finance.model.dto.Status;
import com.javatodev.finance.model.dto.User;
import com.javatodev.finance.model.dto.UserUpdateRequest;
import com.javatodev.finance.model.entity.UserEntity;
import com.javatodev.finance.model.repository.UserRepository;
import com.javatodev.finance.model.rest.response.UserResponse;
import com.javatodev.finance.service.rest.BankingCoreRestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private KeycloakUserService keycloakUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BankingCoreRestClient bankingCoreRestClient;

    @InjectMocks
    private UserService userService;

    private User newUserRequest() {
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setIdentification("199512345678");
        user.setPassword("Str0ngP@ss");
        return user;
    }

    private UserResponse matchingCoreUser() {
        UserResponse response = new UserResponse();
        response.setId(10);
        response.setEmail("john.doe@example.com");
        response.setFirstName("John");
        response.setLastName("Doe");
        response.setIdentificationNumber("199512345678");
        return response;
    }

    @Test
    void createUser_success() {
        User request = newUserRequest();

        UserRepresentation created = new UserRepresentation();
        created.setId("auth-id-1");

        when(keycloakUserService.readUserByEmail("john.doe@example.com"))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.singletonList(created));
        when(bankingCoreRestClient.readUser("199512345678")).thenReturn(matchingCoreUser());
        when(keycloakUserService.createUser(any(UserRepresentation.class))).thenReturn(201);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        assertEquals("auth-id-1", captor.getValue().getAuthId());
        assertEquals(Status.PENDING, captor.getValue().getStatus());
        assertEquals("199512345678", captor.getValue().getIdentification());
    }

    @Test
    void createUser_emailAlreadyRegistered() {
        User request = newUserRequest();
        when(keycloakUserService.readUserByEmail("john.doe@example.com"))
                .thenReturn(Collections.singletonList(new UserRepresentation()));

        assertThrows(UserAlreadyRegisteredException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_invalidEmail() {
        User request = newUserRequest();
        UserResponse coreUser = matchingCoreUser();
        coreUser.setEmail("different@example.com");

        when(keycloakUserService.readUserByEmail("john.doe@example.com"))
                .thenReturn(Collections.emptyList());
        when(bankingCoreRestClient.readUser("199512345678")).thenReturn(coreUser);

        assertThrows(InvalidEmailException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_noMatchingCoreUser() {
        User request = newUserRequest();

        when(keycloakUserService.readUserByEmail("john.doe@example.com"))
                .thenReturn(Collections.emptyList());
        when(bankingCoreRestClient.readUser("199512345678")).thenReturn(new UserResponse());

        assertThrows(InvalidBankingUserException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void readUser_found() {
        UserEntity entity = new UserEntity();
        entity.setId(5L);
        entity.setIdentification("199512345678");
        when(userRepository.findById(5L)).thenReturn(Optional.of(entity));

        User result = userService.readUser(5L);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("199512345678", result.getIdentification());
    }

    @Test
    void readUser_notFound() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.readUser(5L));
    }

    @Test
    void readUsers_success() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setAuthId("auth-id-1");
        entity.setIdentification("199512345678");
        Page<UserEntity> page = new PageImpl<>(Collections.singletonList(entity));

        UserRepresentation representation = new UserRepresentation();
        representation.setEmail("john.doe@example.com");

        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(keycloakUserService.readUser("auth-id-1")).thenReturn(representation);

        List<User> result = userService.readUsers(Pageable.unpaged());

        assertEquals(1, result.size());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
    }

    @Test
    void updateUser_approvedEnablesKeycloakUser() {
        UserEntity entity = new UserEntity();
        entity.setId(2L);
        entity.setAuthId("auth-id-2");
        entity.setStatus(Status.PENDING);

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setStatus(Status.APPROVED);

        UserRepresentation representation = new UserRepresentation();
        representation.setId("auth-id-2");

        when(userRepository.findById(2L)).thenReturn(Optional.of(entity));
        when(keycloakUserService.readUser("auth-id-2")).thenReturn(representation);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(2L, updateRequest);

        assertEquals(Status.APPROVED, result.getStatus());
        ArgumentCaptor<UserRepresentation> captor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(keycloakUserService).updateUser(captor.capture());
        assertEquals(Boolean.TRUE, captor.getValue().isEnabled());
        assertEquals(Boolean.TRUE, captor.getValue().isEmailVerified());
    }

    @Test
    void updateUser_nonApprovedDoesNotTouchKeycloak() {
        UserEntity entity = new UserEntity();
        entity.setId(3L);
        entity.setStatus(Status.PENDING);

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setStatus(Status.DISABLED);

        when(userRepository.findById(3L)).thenReturn(Optional.of(entity));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(3L, updateRequest);

        assertEquals(Status.DISABLED, result.getStatus());
        verify(keycloakUserService, never()).updateUser(any());
        verify(keycloakUserService, never()).readUser(any());
    }

    @Test
    void updateUser_notFound() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setStatus(Status.APPROVED);
        when(userRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(9L, updateRequest));
    }
}

package com.javatodev.finance.service;

import com.javatodev.finance.configuration.keycloak.KeycloakManager;
import com.javatodev.finance.exception.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakUserServiceTest {

    @Mock
    private KeycloakManager keycloakManager;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @InjectMocks
    private KeycloakUserService keycloakUserService;

    @Test
    void createUser_returnsResponseStatus() {
        Response response = Response.status(201).build();
        when(keycloakManager.getKeyCloakInstanceWithRealm()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        UserRepresentation representation = new UserRepresentation();
        when(usersResource.create(representation)).thenReturn(response);

        Integer status = keycloakUserService.createUser(representation);

        assertEquals(201, status);
    }

    @Test
    void readUserByEmail_delegatesToSearch() {
        UserRepresentation representation = new UserRepresentation();
        representation.setEmail("john.doe@example.com");
        when(keycloakManager.getKeyCloakInstanceWithRealm()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.search("john.doe@example.com")).thenReturn(Collections.singletonList(representation));

        List<UserRepresentation> result = keycloakUserService.readUserByEmail("john.doe@example.com");

        assertEquals(1, result.size());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
    }

    @Test
    void readUser_returnsRepresentation() {
        UserResource userResource = org.mockito.Mockito.mock(UserResource.class);
        UserRepresentation representation = new UserRepresentation();
        representation.setId("auth-id-1");

        when(keycloakManager.getKeyCloakInstanceWithRealm()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get("auth-id-1")).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(representation);

        UserRepresentation result = keycloakUserService.readUser("auth-id-1");

        assertEquals("auth-id-1", result.getId());
    }

    @Test
    void readUser_throwsWhenNotFound() {
        when(keycloakManager.getKeyCloakInstanceWithRealm()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get("missing")).thenThrow(new RuntimeException("not found"));

        assertThrows(EntityNotFoundException.class, () -> keycloakUserService.readUser("missing"));
    }

    @Test
    void updateUser_delegatesToKeycloak() {
        UserResource userResource = org.mockito.Mockito.mock(UserResource.class);
        UserRepresentation representation = new UserRepresentation();
        representation.setId("auth-id-1");

        when(keycloakManager.getKeyCloakInstanceWithRealm()).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get("auth-id-1")).thenReturn(userResource);

        keycloakUserService.updateUser(representation);

        verify(userResource).update(representation);
    }
}

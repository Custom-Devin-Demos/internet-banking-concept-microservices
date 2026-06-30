package com.javatodev.finance.model.repository;

import com.javatodev.finance.model.dto.Status;
import com.javatodev.finance.model.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity buildUser(String authId, String identification, Status status) {
        UserEntity entity = new UserEntity();
        entity.setAuthId(authId);
        entity.setIdentification(identification);
        entity.setStatus(status);
        return entity;
    }

    @Test
    void saveAndFindById() {
        UserEntity saved = userRepository.save(buildUser("auth-id-1", "199512345678", Status.PENDING));

        assertNotNull(saved.getId());

        Optional<UserEntity> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("auth-id-1", found.get().getAuthId());
        assertEquals("199512345678", found.get().getIdentification());
        assertEquals(Status.PENDING, found.get().getStatus());
    }

    @Test
    void findAllReturnsPersistedUsers() {
        userRepository.save(buildUser("auth-id-1", "111", Status.PENDING));
        userRepository.save(buildUser("auth-id-2", "222", Status.APPROVED));

        List<UserEntity> all = userRepository.findAll();
        assertEquals(2, all.size());
    }
}

package com.javatodev.finance.repository;

import com.javatodev.finance.model.entity.UserEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false",
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false"
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdentificationNumber_returnsUser() {
        UserEntity entity = new UserEntity();
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEmail("john.doe@example.com");
        entity.setIdentificationNumber("ID123");
        userRepository.save(entity);

        Optional<UserEntity> found = userRepository.findByIdentificationNumber("ID123");
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
    }

    @Test
    void findByIdentificationNumber_missing_returnsEmpty() {
        assertFalse(userRepository.findByIdentificationNumber("does-not-exist").isPresent());
    }
}

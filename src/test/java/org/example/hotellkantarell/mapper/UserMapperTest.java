package org.example.hotellkantarell.mapper;

import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void userToDto_convertsCorrectly() {
        User user = new User(1L, "Test User", "test@example.com", "hashedPassword");
        UserDto dto = userMapper.userToDto(user);

        assertEquals(user.getId(), dto.id());
        assertEquals(user.getName(), dto.name());
        assertEquals(user.getEmail(), dto.email());
    }
}
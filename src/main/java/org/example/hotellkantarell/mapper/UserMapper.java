package org.example.hotellkantarell.mapper;

import org.example.hotellkantarell.dto.UserDto;
import org.example.hotellkantarell.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserDto userToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public User dtoToUser(UserDto userDto) {
        return new User(
                userDto.id(),
                userDto.name(),
                userDto.email());
    }
}

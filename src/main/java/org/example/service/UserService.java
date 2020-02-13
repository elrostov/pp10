package org.example.service;

import org.example.model.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getUsers();
    Optional<UserDto> saveUser(UserDto userDto);
    Optional<UserDto> updateUser(UserDto userDto);
    void deleteUser(long id);
}

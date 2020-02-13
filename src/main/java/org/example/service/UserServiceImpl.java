package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;
import org.example.model.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public Optional<UserDto> saveUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = new User(userDto);
        user.setRoles(Arrays.stream(userDto.getRoles()).map(roleService::getRole).collect(Collectors.toSet()));
        if (userDao.saveUser(user)) {
            user = (User) userDao.loadUserByUsername(user.getUsername());
            userDto.setId(user.getId());
            userDto.setPassword("");
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> updateUser(UserDto userDto) {
        if (!userDto.getPassword().isEmpty()) {
            String hashPassword = passwordEncoder.encode(userDto.getPassword());
            userDto.setPassword(hashPassword);
        } else {
            User userFromDB = (User) userDao.loadUserByUsername(userDto.getUsername());
            userDto.setPassword(userFromDB.getPassword());
        }
        User user = new User(userDto);
        user.setRoles(Arrays.stream(userDto.getRoles()).map(roleService::getRole).collect(Collectors.toSet()));
        if (userDao.updateUser(user)) {
            userDto.setPassword("");
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Override
    public void deleteUser(long id) {
        userDao.deleteUser(id);
    }
}

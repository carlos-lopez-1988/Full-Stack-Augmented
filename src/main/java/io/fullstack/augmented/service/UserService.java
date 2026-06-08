package io.fullstack.augmented.service;

import io.fullstack.augmented.entity.User;
import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User findUserById(Long id);

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}

package io.fullstack.augmented.service.impl;

import io.fullstack.augmented.entity.User;
import io.fullstack.augmented.exception.ResourceNotFoundException;
import io.fullstack.augmented.repository.UserRepository;
import io.fullstack.augmented.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUsers() {
        LOGGER.debug("Fetching all users from repository");
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        LOGGER.debug("Fetching user with id {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public User createUser(User user) {
        LOGGER.info("Creating new user with email {}", user.getEmail());
        try {
            user.setCreatedAt(OffsetDateTime.now());
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.error("Failed to create user due to data integrity violation: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public User updateUser(Long id, User user) {
        LOGGER.info("Updating user with id {}", id);
        User existingUser = findUserById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        LOGGER.info("Deleting user with id {}", id);
        User existingUser = findUserById(id);
        userRepository.delete(existingUser);
    }
}

package io.fullstack.augmented.service;

import io.fullstack.augmented.entity.User;
import io.fullstack.augmented.exception.ResourceNotFoundException;
import io.fullstack.augmented.repository.UserRepository;
import io.fullstack.augmented.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User("testuser", "test@example.com");
        sampleUser.setId(1L);
        sampleUser.setCreatedAt(OffsetDateTime.now());
    }

    @Test
    void shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        var result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleUser, result.get(0));
    }

    @Test
    void shouldReturnUserWhenFoundById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        var result = userService.findUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowWhenUserNotFoundById() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserById(2L));
    }

    @Test
    void shouldCreateUser() {
        var incomingUser = new User("newuser", "new@example.com");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            var saved = invocation.getArgument(0, User.class);
            saved.setId(2L);
            return saved;
        });

        var created = userService.createUser(incomingUser);

        assertNotNull(created.getId());
        assertEquals("newuser", created.getUsername());
        assertNotNull(created.getCreatedAt());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldUpdateExistingUser() {
        var update = new User("updated", "update@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0, User.class));

        var updated = userService.updateUser(1L, update);

        assertEquals("updated", updated.getUsername());
        assertEquals("update@example.com", updated.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldDeleteExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(sampleUser);
    }
}

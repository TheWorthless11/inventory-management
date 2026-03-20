package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_whenUsernameExists_throwsIllegalArgumentException() {
        Users user = new Users();
        user.setUsername("mahhia");

        when(userRepository.findByUsername("mahhia")).thenReturn(Optional.of(new Users()));

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void registerUser_whenRoleInvalid_throwsIllegalArgumentException() {
        Users user = new Users();
        user.setUsername("newuser");
        user.setRole("manager");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void registerUser_whenValid_normalizesRoleAndEncodesPassword() {
        Users user = new Users();
        user.setUsername("newuser");
        user.setRole("seller");
        user.setPassword("plain123");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plain123")).thenReturn("encoded-pass");
        when(userRepository.save(user)).thenReturn(user);

        Users saved = userService.registerUser(user);

        assertEquals("SELLER", saved.getRole());
        assertEquals("encoded-pass", saved.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void getUserByUsername_whenMissing_throwsResourceNotFoundException() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername("ghost"));
    }
}
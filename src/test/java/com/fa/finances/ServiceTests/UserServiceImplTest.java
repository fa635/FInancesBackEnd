package com.fa.finances.ServiceTests;


import com.fa.finances.configurations.JwtUtil;
import com.fa.finances.dto.UserDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.models.User;
import com.fa.finances.repositories.UserRepository;
import com.fa.finances.requests.UserRequest;
import com.fa.finances.services.implementaions.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    private UserRequest userRequest;
    private User user;

    @BeforeEach
    void setUp() {
    	
        userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("john@example.com");
        userRequest.setPassword("password123");

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .passwordHash("hashedPassword")
                .role("USER")
                .build();
    }

    @Test
    void createUser_success() throws FinancesException {
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Long id = userService.create(userRequest);

        assertEquals(user.getId(), id);
        verify(userRepository).existsByEmail(userRequest.getEmail());
        verify(passwordEncoder).encode(userRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_emailExists_throwsException() {
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

        FinancesException exception = assertThrows(FinancesException.class, () -> {
            userService.create(userRequest);
        });

        assertEquals("Email already in use", exception.getMessage());
    }

    @Test
    void signin_success() throws FinancesException {
        Authentication auth = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(user.getEmail());
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("jwt-token");

        String token = userService.signin(userRequest);

        assertEquals("jwt-token", token);
    }

    @Test
    void signin_invalidCredentials_throwsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        FinancesException exception = assertThrows(FinancesException.class, () -> {
            userService.signin(userRequest);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void updateUser_success() throws FinancesException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userRequest.setName("Jane Doe");
        userRequest.setEmail("jane@example.com");

        userService.update(1L, userRequest);

        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class, () -> {
            userService.update(1L, userRequest);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUser_success() throws FinancesException {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_notFound_throwsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        FinancesException exception = assertThrows(FinancesException.class, () -> {
            userService.delete(1L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllUsers_success() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals(user.getEmail(), result.get(0).getEmail());
    }

    @Test
    void getById_success() throws FinancesException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO dto = userService.getById(1L);

        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void getById_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class, () -> {
            userService.getById(1L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getByEmail_success() throws FinancesException {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDTO dto = userService.getByEmail(user.getEmail());

        assertEquals(user.getName(), dto.getName());
    }

    @Test
    void getByEmail_notFound_throwsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class, () -> {
            userService.getByEmail(user.getEmail());
        });

        assertEquals("User not found", exception.getMessage());
    }
}

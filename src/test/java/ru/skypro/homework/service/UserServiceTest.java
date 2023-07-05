package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Role;

import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, imageRepository, passwordEncoder, userMapper);
    }

    @Test
    public void testLoadUserByUsernameReturnsUserDetails() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setRole(Role.USER);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        assertEquals(Role.USER.name(), userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    public void testCreateUserIsOk() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        userService.createUser(user);

        verify(userRepository).save(user);
        verify(passwordEncoder).encode("password");
    }

    @Test
    public void testGetCurrentUsernameReturnsUsername() {
        String username = "testuser";
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(username);

        String currentUsername = userService.getCurrentUsername();

        assertEquals(username, currentUsername);
    }


    @Test
    public void testGetCurrentUserRoleReturnsRole() {
        String role = "ROLE_USER";
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        List<GrantedAuthority> authorities = Arrays.asList(authority);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        String currentUserRole = userService.getCurrentUserRole();

        assertEquals(role, currentUserRole);
    }




}

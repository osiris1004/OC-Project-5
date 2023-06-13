package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    void findById_ExistingUser_ReturnsUserDto() {
        UserService userService = mock(UserService.class);
        UserMapper userMapper = mock(UserMapper.class);
        UserController userController = new UserController(userService, userMapper);

        long userId = 1L;
        User user = new User();
        UserDto userDto = new UserDto();

        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById(String.valueOf(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDto);
    }

    @Test
    void findById_NonExistingUser_ReturnsNotFound() {
        UserService userService = mock(UserService.class);
        UserMapper userMapper = mock(UserMapper.class);
        UserController userController = new UserController(userService, userMapper);

        long userId = 1L;

        when(userService.findById(userId)).thenReturn(null);

        ResponseEntity<?> response = userController.findById(String.valueOf(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() {
        UserService userService = mock(UserService.class);
        UserMapper userMapper = mock(UserMapper.class);
        UserController userController = new UserController(userService, userMapper);

        String invalidId = "abc";

        ResponseEntity<?> response = userController.findById(invalidId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void delete_ValidUserAndAuthenticated_ReturnsOk() {
        UserService userService = mock(UserService.class);
        UserMapper userMapper = mock(UserMapper.class);
        UserController userController = new UserController(userService, userMapper);

        long userId = 1L;
        User user = new User();

        UserDetails userDetails = mock(UserDetails.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(userId)).thenReturn(user);
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(user.getEmail());

        ResponseEntity<?> response = userController.save(String.valueOf(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void delete_ValidUserAndNotAuthenticated_ReturnsUnauthorized() {
        UserService userService = mock(UserService.class);
        UserMapper userMapper = mock(UserMapper.class);
        UserController userController = new UserController(userService, userMapper);

        long userId = 1L;
        User user = new User();

        UserDetails userDetails = mock(UserDetails.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(userId)).thenReturn(user);
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("different-email@example.com");

        ResponseEntity<?> response = userController.save(String.valueOf(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void delete_NonExistingUser_ReturnsNotFound() {
        UserService userService = mock(UserService.class);
        UserMapper userMapper = mock(UserMapper.class);
        UserController userController = new UserController(userService, userMapper);

        long userId = 1L;

        when(userService.findById(userId)).thenReturn(null);

        ResponseEntity<?> response = userController.save(String.valueOf(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void delete_InvalidId_ReturnsBadRequest() {
        UserService userService = mock(UserService.class);
        UserMapper userMapper = mock(UserMapper.class);
        UserController userController = new UserController(userService, userMapper);

        String invalidId = "abc";

        ResponseEntity<?> response = userController.save(invalidId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }
}


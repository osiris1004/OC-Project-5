package com.openclassrooms.starterjwt.controllers;
import com.github.javafaker.Faker;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserController underTest;

    private final Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        underTest = new UserController(userService, userMapper);
    }

    @Test
    void testFindById_ExistingUser_ReturnsUserDto() {
      
        long userId = 1L;
        User user = new User();
        UserDto userDto = new UserDto();

        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = underTest.findById(String.valueOf(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDto);
    }

    @Test
    void testFindById_NonExistingUser_ReturnsNotFound() {
        long userId = 1L;

        when(userService.findById(userId)).thenReturn(null);

        ResponseEntity<?> response = underTest.findById(String.valueOf(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void testFindById_InvalidId_ReturnsBadRequest() {
    String invalidId = faker.idNumber().invalid();

    ResponseEntity<?> response = underTest.findById(invalidId);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNull();
    }

    @Test
    void testDelete_ValidUserAndAuthenticated_ReturnsOk() {
        long userId = faker.number().randomNumber();
        User user = new User();

        UserDetails userDetails = mock(UserDetails.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(userId)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(user.getEmail());

        ResponseEntity<?> response = underTest.save(String.valueOf(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void testDelete_ValidUserAndNotAuthenticated_ReturnsUnauthorized() {
        long userId = faker.number().randomNumber();
        User user = new User();

        UserDetails userDetails = mock(UserDetails.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(userId)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("different-email@example.com");

        ResponseEntity<?> response = underTest.save(String.valueOf(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void testDelete_NonExistingUser_ReturnsNotFound() {
        long userId = faker.number().randomNumber();

        when(userService.findById(userId)).thenReturn(null);

        ResponseEntity<?> response = underTest.save(String.valueOf(userId));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void testDelete_InvalidId_ReturnsBadRequest() {
        String invalidId = faker.idNumber().invalid();

        ResponseEntity<?> response = underTest.save(invalidId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }
}


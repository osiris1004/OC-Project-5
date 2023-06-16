package com.openclassrooms.starterjwt.controllers;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("gym@studio.com");
        loginRequest.setPassword("test!1234");

        ResponseEntity<JwtResponse> loginResponse  = restTemplate.postForEntity("/api/auth/login", loginRequest, JwtResponse.class);
        jwtToken = "Bearer " + loginResponse.getBody().getToken();
    }
    @Test
    public void testGetUserById() {
        // Set the Authorization header with the JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange("/api/user/2", HttpMethod.GET, entity, UserDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteUserById() {
        // Set the Authorization header with the JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Delete the user (if it doesn't participate in any session ¯\_(ツ)_/¯)
        restTemplate.exchange("/api/user/2", HttpMethod.DELETE, entity, Void.class);

        /*
            If you ask why a try/catch as a assertion for a test,
            it's because some incompetents people don't handle exceptions in their login controller,
            in case of idk, user not found or some random shenanigans
        */
        try {
            String email = "gym@studio.com";
            String password = "test!1234";

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);

            ResponseEntity<JwtResponse> responseEntity = restTemplate.postForEntity("/api/auth/login", loginRequest, JwtResponse.class);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}
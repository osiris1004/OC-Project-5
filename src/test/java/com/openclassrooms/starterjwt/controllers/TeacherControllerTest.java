package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
class TeacherControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        ResponseEntity<JwtResponse> loginResponse  = restTemplate.postForEntity("/api/auth/login", loginRequest, JwtResponse.class);
        jwtToken = "Bearer " + loginResponse.getBody().getToken();
    }
    @Test
    public void testGetTeacherById() {
        // Set the Authorization header with the JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<TeacherDto> responseEntity = restTemplate.exchange("/api/teacher/1", HttpMethod.GET, entity, TeacherDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Margot", responseEntity.getBody().getFirstName());
    }

    @Test
    public void testGetAllTeachers() {
        // Set the Authorization header with the JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<TeacherDto[]> responseEntity = restTemplate.exchange("/api/teacher", HttpMethod.GET, entity, TeacherDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().length);
    }
}
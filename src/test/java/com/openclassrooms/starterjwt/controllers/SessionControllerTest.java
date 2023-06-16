package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.SessionDto;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
public class SessionControllerTest {

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
    public void testFindById() throws Exception {
        // Replace with a valid session ID from your test data
        Long sessionId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/session/" + sessionId, HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Optionally, you can deserialize the JSON response to a SessionDto and perform more assertions
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;
        SessionDto sessionDto = objectMapper.readValue(response.getBody(), SessionDto.class);
        assertEquals(sessionId, sessionDto.getId());
    }
    @Test
    public void testFindAllSessions() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/session", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Deserialize the JSON response to a List of SessionDto and perform more assertions
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<SessionDto> sessions = objectMapper.readValue(response.getBody(), new TypeReference<List<SessionDto>>() {});
        assertTrue(sessions.size() == 2);
    }


    @Test
    public void testCreate() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test Session " + UUID.randomUUID());
        sessionDto.setDescription("Test Session Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDate(new Date());

        HttpEntity<SessionDto> entity = new HttpEntity<>(sessionDto, headers);

        ResponseEntity<SessionDto> response = restTemplate.postForEntity("/api/session", entity, SessionDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void testUpdate() throws Exception {
        // Replace with a valid session ID from your test data
        Long sessionId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(sessionId);
        sessionDto.setName("Updated Test Session");
        sessionDto.setDescription("Updated Test Session Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDate(new Date());
        sessionDto.setUpdatedAt(LocalDateTime.now());
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUsers(Arrays.asList(1L));

        HttpEntity<SessionDto> entity = new HttpEntity<>(sessionDto, headers);

        ResponseEntity<SessionDto> responsePut = restTemplate.exchange("/api/session/" + sessionId, HttpMethod.PUT, entity, SessionDto.class);
        assertEquals(HttpStatus.OK, responsePut.getStatusCode());

        // Fetch the updated session and check if the name was updated
        ResponseEntity<String> response = restTemplate.exchange("/api/session/" + sessionId, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;
        SessionDto updatedSession = objectMapper.readValue(response.getBody(), SessionDto.class);
        assertEquals(sessionDto.getName(), updatedSession.getName());
    }

    @Test
    public void testSave() {
        // Replace with a valid session ID from your test data
        Long sessionId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange("/api/session/" + sessionId, HttpMethod.DELETE, entity, Void.class);

        // Verify the session was deleted by fetching it
        ResponseEntity<String> response = restTemplate.exchange("/api/session/" + sessionId, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testParticipate() {
        // Replace with valid session and user IDs from your test data
        Long sessionId = 2L;
        Long userId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("/api/session/" + sessionId + "/participate/" + userId, HttpMethod.POST, entity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Fetch the session and verify the participation (depends on the data structure)
    }

    @Test
    public void testNoLongerParticipate() {
        // Replace with valid session and user IDs from your test data
        Long sessionId = 1L;
        Long userId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("/api/session/" + sessionId + "/participate/" + userId, HttpMethod.DELETE, entity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Fetch the session and verify the participation was removed (depends on the data structure)
    }
}
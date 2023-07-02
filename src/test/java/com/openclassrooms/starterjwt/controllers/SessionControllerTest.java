package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.*;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;


    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        restTemplate.postForEntity("/api/auth/login", loginRequest,JwtResponse.class);
    }

    @Test
    public void testFindById() throws Exception {
        MvcResult requestResult = mockMvc.perform(get("/api/session/1")
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();
        assertEquals(200, requestResult.getResponse().getStatus());

        String result = requestResult.getResponse().getContentAsString();
        SessionDto resultSessionDto = objectMapper.readValue(result, SessionDto.class);
        assertNotNull(resultSessionDto);
        assertEquals(1, resultSessionDto.getId());
    }

    @Test
    public void testFindByIdSessionNull() throws Exception {
        MvcResult requestResult = mockMvc.perform(get("/api/session/3")
                    .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andReturn();
        assertEquals(404, requestResult.getResponse().getStatus());
        assertTrue(requestResult.getResponse().getContentAsString().isEmpty());
    }
    @Test
    public void testFindByIdThrownError() throws Exception {
        MvcResult requestResult = mockMvc.perform(get("/api/session/test")
                        .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();
        assertEquals(400, requestResult.getResponse().getStatus());
    }

    @Test
    public void testFindAllSessions() throws Exception {
        MvcResult requestResult = mockMvc.perform(get("/api/session")
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();
        assertEquals(200, requestResult.getResponse().getStatus());

        String result = requestResult.getResponse().getContentAsString();
        List<SessionDto> resultSessionDto = objectMapper.readValue(result, new TypeReference<List<SessionDto>>() {
        });
        assertNotNull(resultSessionDto);
        assertEquals(2, resultSessionDto.size());
    }

    @Test
    public void testCreate() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test Session " + UUID.randomUUID());
        sessionDto.setDescription("Test Session Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDate(new Date());

        MvcResult requestResult = mockMvc.perform(post("/api/session")
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andReturn();
        assertEquals(200, requestResult.getResponse().getStatus());
        String result = requestResult.getResponse().getContentAsString();
        SessionDto resultSessionDto = objectMapper.readValue(result, SessionDto.class);

        assertEquals(sessionDto.getName(), resultSessionDto.getName());
    }

    @Test
    public void testUpdate() throws Exception {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1l);
        sessionDto.setName("Updated Test Session");
        sessionDto.setDescription("Updated Test Session Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDate(new Date());
        sessionDto.setUpdatedAt(LocalDateTime.now());
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUsers(Arrays.asList(1L));

        MvcResult requestResult = mockMvc.perform(put("/api/session/1")
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andReturn();

        assertEquals(200, requestResult.getResponse().getStatus());
        String result = requestResult.getResponse().getContentAsString();
        SessionDto resultSessionDto = objectMapper.readValue(result, SessionDto.class);

        assertEquals(sessionDto.getName(), resultSessionDto.getName());
    }

    @Test
    public void testUpdateThrownError() throws Exception {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1l);
        sessionDto.setName("Updated Test Session");
        sessionDto.setDescription("Updated Test Session Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDate(new Date());
        sessionDto.setUpdatedAt(LocalDateTime.now());
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUsers(Arrays.asList(1L));

        MvcResult requestResult = mockMvc.perform(put("/api/session/test")
                        .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andReturn();

        assertEquals(400, requestResult.getResponse().getStatus());

    }



    @Test
    public void testSave() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/session/1")
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();

        assertEquals(404, response.getResponse().getStatus());
    }

    @Test
    public void testSaveSessionNull() throws Exception {
        MvcResult response = mockMvc.perform(delete("/api/session/4")
                        .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();

        assertEquals(404, response.getResponse().getStatus());

    }

    @Test
    public void testSaveThrownError() throws Exception {
        MvcResult response = mockMvc.perform(delete("/api/session/test")
                        .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();
        assertEquals(400, response.getResponse().getStatus());
    }

    @Test
    public void testParticipate() throws Exception {
        Long sessionId = 2L;
        Long userId = 1L;

        MvcResult requestResult = mockMvc.perform(post("/api/session/" + sessionId + "/participate/" + userId)
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();
        assertEquals(200, requestResult.getResponse().getStatus());

        MvcResult response = mockMvc.perform(get("/api/session/" + sessionId)
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();

        String result = response.getResponse().getContentAsString();

        SessionDto resultSessionDto = objectMapper.readValue(result, SessionDto.class);
        assertEquals(userId, resultSessionDto.getUsers().get(0));
    }

    @Test
    public void testParticipateThrownError() throws Exception {

        Long userId = 1L;

        MvcResult requestResult = mockMvc.perform(post("/api/session/" + "sessionId" + "/participate/" + "userId")
                        .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();
        assertEquals(400, requestResult.getResponse().getStatus());

        MvcResult response = mockMvc.perform(get("/api/session/" + "sessionId")
                        .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();

        assertEquals(400, response.getResponse().getStatus());
    }

    @Test
    public void testNoLongerParticipate() throws Exception {
        Long sessionId = 1L;
        Long userId = 1L;

        MvcResult requestResult = mockMvc.perform(delete("/api/session/" + sessionId + "/participate/" + userId)
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();
        assertEquals(200, requestResult.getResponse().getStatus());

        MvcResult response = mockMvc.perform(get("/api/session/" + sessionId)
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();

        String result = response.getResponse().getContentAsString();

        SessionDto resultSessionDto = objectMapper.readValue(result, SessionDto.class);
        assertEquals(0, resultSessionDto.getUsers().size());

    }

    @Test
    public void testNoLongerParticipateThrownError() throws Exception {
        Long sessionId = 1L;
        Long userId = 1L;

        MvcResult requestResult = mockMvc.perform(delete("/api/session/" + "sessionId" + "/participate/" + "userId")
                        .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();
        assertEquals(400, requestResult.getResponse().getStatus());

        MvcResult response = mockMvc.perform(get("/api/session/" + "sessionId")
                        .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn();

        assertEquals(400, response.getResponse().getStatus());

    }
}
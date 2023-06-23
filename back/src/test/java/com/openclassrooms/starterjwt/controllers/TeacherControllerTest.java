package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("gym@studio.com");
        loginRequest.setPassword("test!1234");
        restTemplate.postForEntity("/api/auth/login", loginRequest, JwtResponse.class);
    }

    @Test
    public void testGetTeacherById() throws Exception {
        MvcResult requestResult = mockMvc.perform(get("/api/teacher/1")
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is(200)).andReturn();
        String result = requestResult.getResponse().getContentAsString();
        TeacherDto resultTeacher = objectMapper.readValue(result, TeacherDto.class);
        assertEquals("Margot", resultTeacher.getFirstName());
    }

    @Test
    public void testGetAllTeachers() throws Exception {
        MvcResult requestResult = mockMvc.perform(get("/api/teacher")
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is(200)).andReturn();
        String result = requestResult.getResponse().getContentAsString();
        List<TeacherDto> resultTeacher = objectMapper.readValue(result, new TypeReference<List<TeacherDto>>() {});
        assertEquals(2, resultTeacher.size());
    }
}
package com.openclassrooms.starterjwt.controllers;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
//test
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext

@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("gym@studio.com");
        loginRequest.setPassword("test!1234");

        ResponseEntity<JwtResponse> loginResponse  = restTemplate.postForEntity("/api/auth/login", loginRequest, JwtResponse.class);
        String jwtToken =  loginResponse.getBody().getToken();
        // try{

        // }catch(){
        
        // }
        
        System.out.println(
            "------------------------------->"+"\n"+"\n"+
            jwtToken
            +"\n"+"\n"+"-------------------------------->"
            );
    }
    @Test
    public void testGetUserById() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/user/2")
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()));
        resultActions.andExpect(status().is(200));
    }

     @Test
    public void testDeleteUserById() throws Exception{
      ResultActions resultActions = mockMvc.perform(delete("/api/user/2")
                .with(SecurityMockMvcRequestPostProcessors.user("gym@studio.com"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is(200));
      resultActions.andExpect(status().isOk());
    }
}
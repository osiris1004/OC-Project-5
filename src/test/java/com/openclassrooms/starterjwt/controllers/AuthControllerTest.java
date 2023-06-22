package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/* ! @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
public class AuthControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRegisterUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("Myname");
        signupRequest.setLastName("Anothername");

        ResponseEntity<?> responseEntity = restTemplate.postForEntity("/api/auth/register", signupRequest, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testAuthenticateUser() {
        String email = "yoga@studio.com";
        String password = "test!1234";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        ResponseEntity<JwtResponse> responseEntity = restTemplate.postForEntity("/api/auth/login", loginRequest, JwtResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(email, responseEntity.getBody().getUsername());
    }
} */

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Optional;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest {

/*     @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

     @MockBean
    private  Authentication authentication;
    

    @Autowired
        private ObjectMapper objectMapper;

    @Test
    public void testAuthenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();//LoginRequest("test@example.com", "password");
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");


        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "John", "Doe",false, "password");

      
        //authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword());

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("test-token");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.jwt").value("test-token"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
                //.andExpect(jsonPath("$.isAdmin").value(false));
    }

    @Test
    public void testRegisterUser() throws Exception {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setFirstName("Myname");
        signUpRequest.setLastName("Anothername");

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encoded-password");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }
 */

  @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    // @Test
    // public void testAuthenticateUser() throws Exception {
    //     String email = "test@example.com";
    //     String password = "password";
    //     String jwt = "mocked-jwt-token";
    //     UserDetailsImpl userDetails = new UserDetailsImpl(1L, email, "John", "Doe", false ,"password");

    //     LoginRequest loginRequest = new LoginRequest();
    //     loginRequest.setEmail(email);
    //     loginRequest.setPassword(password);

    //     Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
    //     SecurityContextHolder.getContext().setAuthentication(authentication);

    //     Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class)))
    //             .thenReturn(authentication);
    //     Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwt);
    //     Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
    //     Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

    //     mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(loginRequest)))
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").value(jwt))
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.isAdmin").value(false));
    //     // Add additional assertions as needed
    // }

    @Test
    public void testRegisterUser() throws Exception {
        String email = "test@example.com";
        String password = "password";
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setFirstName("john");
        signupRequest.setLastName("doe");
        signupRequest.setPassword(password);

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encoded-password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("User registered successfully!"));
        // Add additional assertions as needed

           System.out.println(
                "------------------------------->" + "\n" + "\n" +
                        256
                        + "\n" + "\n" + "-------------------------------->");
    }

}
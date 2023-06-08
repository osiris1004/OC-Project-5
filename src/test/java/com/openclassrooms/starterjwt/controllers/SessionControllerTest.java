package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
public class SessionControllerTest {
    private SessionController sessionController;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sessionController = new SessionController(sessionService, sessionMapper);
    }

    @Test
    public void findById_ValidId_ReturnsSessionDto() {

        String id = "1";
        Session session = new Session();
        session.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());


        ResponseEntity<?> responseEntity = sessionController.findById(id);


        verify(sessionService).getById(1L);
        verify(sessionMapper).toDto(session);
        assert responseEntity.getStatusCode() == HttpStatus.OK;
        assert responseEntity.getBody() != null;
        assert responseEntity.getBody() instanceof SessionDto;
    }

    @Test
    public void findById_InvalidId_ReturnsNotFound() {

        String id = "invalid";

        when(sessionService.getById(anyLong())).thenReturn(null);


        ResponseEntity<?> responseEntity = sessionController.findById(id);


        verify(sessionService).getById(anyLong());
        assert responseEntity.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void findAll_ReturnsListOfSessionDtos() {

        Session session1 = new Session();
        session1.setId(1L);
        Session session2 = new Session();
        session2.setId(2L);
        List<Session> sessions = Arrays.asList(session1, session2);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(Arrays.asList(new SessionDto(), new SessionDto()));


        ResponseEntity<?> responseEntity = sessionController.findAll();


        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessions);
        assert responseEntity.getStatusCode() == HttpStatus.OK;
        assert responseEntity.getBody() != null;
        assert responseEntity.getBody() instanceof List;
        List<?> responseList = (List<?>) responseEntity.getBody();
        assert responseList.size() == 2;
        assert responseList.get(0) instanceof SessionDto;
    }

    @Test
    public void create_ValidSessionDto_ReturnsCreatedSessionDto() {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Session 1");
        Session session = new Session();
        session.setId(1L);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());


        ResponseEntity<?> responseEntity = sessionController.create(sessionDto);


        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).create(session);
        verify(sessionMapper).toDto(session);
        assert responseEntity.getStatusCode() == HttpStatus.OK;
        assert responseEntity.getBody() != null;
        assert responseEntity.getBody() instanceof SessionDto;
    }

    @Test
    public void update_ValidIdAndSessionDto_ReturnsUpdatedSessionDto() {

        String id = "1";
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        Session session = new Session();
        session.setId(1L);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());


        ResponseEntity<?> responseEntity = sessionController.update(id, sessionDto);


        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).update(1L, session);
        verify(sessionMapper).toDto(session);
        assert responseEntity.getStatusCode() == HttpStatus.OK;
        assert responseEntity.getBody() != null;
        assert responseEntity.getBody() instanceof SessionDto;
    }

    @Test
    public void save_ValidId_ReturnsOk() {

        String id = "1";
        Session session = new Session();
        session.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);


        ResponseEntity<?> responseEntity = sessionController.save(id);


        verify(sessionService).getById(1L);
        verify(sessionService).delete(1L);
        assert responseEntity.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void participate_ValidIds_ReturnsOk() {

        String sessionId = "1";
        String userId = "2";


        ResponseEntity<?> responseEntity = sessionController.participate(sessionId, userId);


        verify(sessionService).participate(1L, 2L);
        assert responseEntity.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void noLongerParticipate_ValidIds_ReturnsOk() {

        String sessionId = "1";
        String userId = "2";


        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(sessionId, userId);


        verify(sessionService).noLongerParticipate(1L, 2L);
        assert responseEntity.getStatusCode() == HttpStatus.OK;
    }
}
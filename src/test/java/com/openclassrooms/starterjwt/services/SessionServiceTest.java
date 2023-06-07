package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


class SessionServiceTest {

    private SessionService underTest;

    private SessionRepository sessionRepository;

    private UserRepository userRepository;


    @BeforeEach
    public void setup() {
        sessionRepository = Mockito.mock(SessionRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        underTest = new SessionService(sessionRepository, userRepository);
    }

    @Test
    void create() {
        Session session = Session.builder()
                .name("Test Session")
                .date(new Date())
                .description("This is a test session")
                .teacher(new Teacher())
                .build();


        //!when
         underTest.create(session);

        //!then
        ArgumentCaptor<Session> sessionArgumentCaptor  = ArgumentCaptor.forClass(Session.class);

        verify(sessionRepository).save(sessionArgumentCaptor .capture());//!studentArgumentCaptor.capture() get the argument

        //!captureStudent  is the student ....Repository(student) receive or what the service receive
        Session captureSession = sessionArgumentCaptor.getValue();

        //!this v student is the want underTest.addStudent(student); receive
        assertThat(captureSession).isEqualTo(session);

    }

    @Test
    void delete() {
        // !given
        Long sessionId = 1L;
        // Call the delete method
        underTest.delete(sessionId);
        // Verify that the deleteById method is called with the correct argument
        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    void findAll() {

        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());


        Mockito.when(sessionRepository.findAll()).thenReturn(sessions);


        List<Session> result = underTest.findAll();


        Mockito.verify(sessionRepository).findAll();


        Assertions.assertEquals(sessions.size(), result.size());
        Assertions.assertTrue(result.containsAll(sessions));
    }

    @Test
    void getById() {
        Long sessionId = 1L;


        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());


        Session result = underTest.getById(sessionId);


        Mockito.verify(sessionRepository).findById(sessionId);


        Assertions.assertNull(result);
    }

    @Test
    void update() {
        Long sessionId = 1L;
        Session sessionToUpdate = new Session();
        sessionToUpdate.setId(sessionId);


        Mockito.when(sessionRepository.save(sessionToUpdate)).thenReturn(sessionToUpdate);


        Session result = underTest.update(sessionId, sessionToUpdate);


        Mockito.verify(sessionRepository).save(sessionToUpdate);


        Assertions.assertEquals(sessionToUpdate, result);
    }

    @Test
    void participate() {
        Long sessionId = 1L;
        Long userId = 1L;


        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());


        User user = new User();
        user.setId(userId);


        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));


        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        underTest.participate(sessionId, userId);


        Mockito.verify(sessionRepository).findById(sessionId);


        Mockito.verify(userRepository).findById(userId);


        Assertions.assertTrue(session.getUsers().contains(user));


        Mockito.verify(sessionRepository).save(session);
    }

    @Test
    public void testNoLongerParticipate() {
        Long sessionId = 1L;
        Long userId = 1L;


        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());


        User user = new User();
        user.setId(userId);
        session.getUsers().add(user);


        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));


        underTest.noLongerParticipate(sessionId, userId);


        Mockito.verify(sessionRepository).findById(sessionId);


        Assertions.assertFalse(session.getUsers().contains(user));


        Mockito.verify(sessionRepository).save(session);
    }

    @Test
    public void testNoLongerParticipate_SessionNotFound() {
        Long sessionId = 1L;
        Long userId = 1L;


        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());


        Assertions.assertThrows(NotFoundException.class, () -> underTest.noLongerParticipate(sessionId, userId));


        Mockito.verify(sessionRepository).findById(sessionId);


        Mockito.verify(sessionRepository, Mockito.never()).save(Mockito.any(Session.class));
    }

    @Test
    public void testNoLongerParticipate_NotParticipating() {
        Long sessionId = 1L;
        Long userId = 1L;


        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());


        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));


        Assertions.assertThrows(BadRequestException.class, () -> underTest.noLongerParticipate(sessionId, userId));


        Mockito.verify(sessionRepository).findById(sessionId);


        Mockito.verify(sessionRepository, Mockito.never()).save(Mockito.any(Session.class));
    }
}
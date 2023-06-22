package com.openclassrooms.starterjwt.services;

import com.github.javafaker.Faker;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private SessionService underTest;

    private final Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        underTest = new SessionService(sessionRepository, userRepository);
    }

    @Test
    void testCreate() {
        // !given
        Session session = Session.builder()
                .name(faker.name().name())
                .date(new Date())
                .description(faker.avatar().toString())
                .teacher(new Teacher())
                .build();
        underTest.create(session);

        // !then
        ArgumentCaptor<Session> sessionArgumentCaptor = ArgumentCaptor.forClass(Session.class);

        verify(sessionRepository).save(sessionArgumentCaptor.capture());// !studentArgumentCaptor.capture() get the
                                                                        // argument

        // !captureStudent is the student ....Repository(student) receive or what the
        // service receive
        Session captureSession = sessionArgumentCaptor.getValue();

        // !this v student is the want underTest.addStudent(student); receive
        assertThat(captureSession).isEqualTo(session);

    }

    @Test
    void testDelete() {
        // !given
        Long sessionId = 1L;
        // !then
        underTest.delete(sessionId);
        // ! Verify that the deleteById method is called with the correct argument
        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    void testFindAll() {

        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());

        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = underTest.findAll();

        verify(sessionRepository).findAll();

        Assertions.assertEquals(sessions.size(), result.size());
        Assertions.assertTrue(result.containsAll(sessions));
    }

    @Test
    void testGetById() {
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        Session result = underTest.getById(sessionId);
        verify(sessionRepository).findById(sessionId);
        Assertions.assertNull(result);
    }

    @Test
    void testUpdate() {
        Long sessionId = 1L;
        Session sessionToUpdate = new Session();
        sessionToUpdate.setId(sessionId);

        when(sessionRepository.save(sessionToUpdate)).thenReturn(sessionToUpdate);
        Session result = underTest.update(sessionId, sessionToUpdate);
        verify(sessionRepository).save(sessionToUpdate);
        Assertions.assertEquals(sessionToUpdate, result);
    }

    @Test
    void testParticipate() {
        Long sessionId = 1L;
        Long userId = 1L;

        Session session = new Session();
        session.setId(sessionId);

        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        underTest.participate(sessionId, userId);
        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
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

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        underTest.noLongerParticipate(sessionId, userId);
        verify(sessionRepository).findById(sessionId);
        Assertions.assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    public void testNoLongerParticipate_SessionNotFound() {
        Long sessionId = 1L;
        Long userId = 1L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> underTest.noLongerParticipate(sessionId, userId));
        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testNoLongerParticipate_NotParticipating() {
        Long sessionId = 1L;
        Long userId = 1L;

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        Assertions.assertThrows(BadRequestException.class, () -> underTest.noLongerParticipate(sessionId, userId));
        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).save(any(Session.class));
    }
}
package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService underTest;

    private UserRepository userRepository;


    @BeforeEach
    public void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        underTest = new UserService(userRepository);
    }

    @Test
    void delete() {
        Long userId = 1L;


        underTest.delete(userId);


        Mockito.verify(userRepository).deleteById(userId);
    }

    @Test
    public void testFindById() {
        Long userId = 1L;


        User user = new User();


        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        User result = underTest.findById(userId);


        Mockito.verify(userRepository).findById(userId);


        Assertions.assertEquals(user, result);
    }

    @Test
    public void testFindById_UserNotFound() {
        Long userId = 1L;


        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());


        User result = underTest.findById(userId);
        Assertions.assertNull(result);


        Mockito.verify(userRepository).findById(userId);
    }
}
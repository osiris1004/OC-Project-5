package com.openclassrooms.starterjwt.services;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService underTest;


    @BeforeEach
    public void setup() {
        underTest = new UserService(userRepository);
    }

    @Test
    void delete() {
        Long userId = 1L;

        underTest.delete(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testFindById() {
        Long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = underTest.findById(userId);

        verify(userRepository).findById(userId);

        Assertions.assertEquals(user, result);
    }

    @Test
    public void testFindById_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User result = underTest.findById(userId);

        Assertions.assertNull(result);

        verify(userRepository).findById(userId);
    }
}
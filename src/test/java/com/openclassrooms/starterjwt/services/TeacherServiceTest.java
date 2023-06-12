package com.openclassrooms.starterjwt.services;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;
    private TeacherService underTest;

   
    @BeforeEach
    public void setup(){
        underTest = new TeacherService(teacherRepository);
    }

    @Test
    void testFindAll() {

        //! give 
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher());
        teachers.add(new Teacher());


        //! when then 
        when(teacherRepository.findAll()).thenReturn(teachers);


        //! when execute underTest method
        List<Teacher> result = underTest.findAll();


        verify(teacherRepository).findAll();

        //! then expected
        Assertions.assertEquals(teachers, result);
    }

    @Test
    public void testFindById() {
        Long teacherId = 1L;


        Teacher teacher = new Teacher();


        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));


        Teacher result = underTest.findById(teacherId);


        verify(teacherRepository).findById(teacherId);


        Assertions.assertEquals(teacher, result);
    }

    @Test
    public void testFindById_TeacherNotFound() {
        Long teacherId = 1L;


        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());


        Teacher result = underTest.findById(teacherId);


        verify(teacherRepository).findById(teacherId);

        
        Assertions.assertNull(result);
    }
}
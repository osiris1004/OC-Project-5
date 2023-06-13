package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TeacherServiceTest {

    private TeacherService underTest;

    private TeacherRepository teacherRepository;

    @BeforeEach
    public void setup(){
        teacherRepository =  Mockito.mock(TeacherRepository.class);
        underTest = new TeacherService(teacherRepository);
    }

    @Test
    void findAll() {


        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher());
        teachers.add(new Teacher());


        Mockito.when(teacherRepository.findAll()).thenReturn(teachers);


        List<Teacher> result = underTest.findAll();


        Mockito.verify(teacherRepository).findAll();


        Assertions.assertEquals(teachers, result);
    }

    @Test
    public void testFindById() {
        Long teacherId = 1L;


        Teacher teacher = new Teacher();


        Mockito.when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));


        Teacher result = underTest.findById(teacherId);


        Mockito.verify(teacherRepository).findById(teacherId);


        Assertions.assertEquals(teacher, result);
    }

    @Test
    public void testFindById_TeacherNotFound() {
        Long teacherId = 1L;


        Mockito.when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());


        Teacher result = underTest.findById(teacherId);
        Assertions.assertNull(result);


        Mockito.verify(teacherRepository).findById(teacherId);
    }
}
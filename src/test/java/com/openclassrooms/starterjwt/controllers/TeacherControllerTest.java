package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeacherControllerTest {
    private TeacherController teacherController;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        teacherController = new TeacherController(teacherService, teacherMapper);
    }

    @Test
    public void findById_ValidId_ReturnsTeacherDto() {
        // Arrange
        String id = "1";
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(new TeacherDto());

        // Act
        ResponseEntity<?> responseEntity = teacherController.findById(id);

        // Assert
        verify(teacherService).findById(1L);
        verify(teacherMapper).toDto(teacher);
        assert responseEntity.getStatusCode() == HttpStatus.OK;
        assert responseEntity.getBody() != null;
        assert responseEntity.getBody() instanceof TeacherDto;
    }

    @Test
    public void findById_NonExistingId_ReturnsNotFound() {
        // Arrange
        String id = "1";

        when(teacherService.findById(anyLong())).thenReturn(null);

        // Create an instance of TeacherController
        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);

        // Act
        ResponseEntity<?> responseEntity = teacherController.findById(id);

        // Assert
        verify(teacherService).findById(anyLong());
        assert responseEntity.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void findAll_ReturnsListOfTeacherDtos() {
        // Arrange
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        when(teacherService.findAll()).thenReturn(teachers);

        // Create a list of TeacherDto objects to be returned by teacherMapper.toDto(teachers)
        List<TeacherDto> expectedTeacherDtos = Arrays.asList(new TeacherDto(), new TeacherDto());
        when(teacherMapper.toDto(teachers)).thenReturn(expectedTeacherDtos);

        // Create an instance of TeacherController
        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);

        // Act
        ResponseEntity<?> responseEntity = teacherController.findAll();

        // Assert
        verify(teacherService).findAll();
        verify(teacherMapper).toDto(teachers);
        assert responseEntity.getStatusCode() == HttpStatus.OK;
        assert responseEntity.getBody() != null;
        assert responseEntity.getBody() instanceof List;
        List<?> responseList = (List<?>) responseEntity.getBody();
        assert responseList.size() == expectedTeacherDtos.size();
        assert responseList.get(0) instanceof TeacherDto;
    }
}
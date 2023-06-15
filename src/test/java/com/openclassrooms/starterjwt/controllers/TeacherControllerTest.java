package com.openclassrooms.starterjwt.controllers;
import com.github.javafaker.Faker;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.*;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TeacherControllerTest {

     @Autowired
        private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    private TeacherController underTest;

    private final Faker faker = new Faker();


    @BeforeEach
    public void setup() {
        underTest = new TeacherController(teacherService, teacherMapper);
    }

  @Test
public void testFindById() throws Exception {
    TeacherDto teacherDto = new TeacherDto();
    teacherDto.setId(1L);
    teacherDto.setFirstName("John Doe");
      teacherDto.setLastName("John Doe");

    Teacher teacher = new Teacher();
    teacher.setId(1L);
    teacher.setFirstName("John Doe");
    teacher.setLastName("John Do");

    when(teacherService.findById(1L)).thenReturn(teacher);
    when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

    mockMvc.perform(get("/api/teacher/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("John Doe")));

    verify(teacherService, times(1)).findById(1L);
    verify(teacherMapper, times(1)).toDto(teacher);
}

@Test
public void testFindAll() throws Exception {
    TeacherDto teacherDto1 = new TeacherDto();
    teacherDto1.setId(1L);
    teacherDto1.setFirstName("John Doe");

    TeacherDto teacherDto2 = new TeacherDto();
    teacherDto2.setId(2L);
    teacherDto2.setFirstName("Jane Smith");

    Teacher teacher1 = new Teacher();
    teacher1.setId(1L);
    teacher1.setFirstName("John Doe");

    Teacher teacher2 = new Teacher();
    teacher2.setId(2L);
    teacher2.setFirstName("Jane Smith");

    List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
    List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);

    when(teacherService.findAll()).thenReturn(teachers);
    when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

    mockMvc.perform(get("/api/teacher"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("John Doe")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is("Jane Smith")));

    verify(teacherService, times(1)).findAll();
    verify(teacherMapper, times(1)).toDto(teachers);
}
}
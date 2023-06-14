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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {
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
    public void testFindById_ValidId_ReturnsTeacherDto() {

        String id = "1";
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(new TeacherDto());


        ResponseEntity<?> responseEntity = underTest.findById(id);


        verify(teacherService).findById(1L);
        verify(teacherMapper).toDto(teacher);
        assert responseEntity.getStatusCode() == HttpStatus.OK;
        assert responseEntity.getBody() != null;
        assert responseEntity.getBody() instanceof TeacherDto;
    }

    @Test
    public void testFindById_NonExistingId_ReturnsNotFound() {

        String id = faker.number().digit();

        when(teacherService.findById(anyLong())).thenReturn(null);

        ResponseEntity<?> responseEntity = underTest.findById(id);


        verify(teacherService).findById(anyLong());
        assert responseEntity.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void testFindAll_ReturnsListOfTeacherDtos() {

        Teacher teacher1 = new Teacher();
        teacher1.setId(faker.number().randomNumber());
        Teacher teacher2 = new Teacher();
        teacher2.setId(faker.number().randomNumber());
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        when(teacherService.findAll()).thenReturn(teachers);


        List<TeacherDto> expectedTeacherDtos = Arrays.asList(new TeacherDto(), new TeacherDto());
        when(teacherMapper.toDto(teachers)).thenReturn(expectedTeacherDtos);

        ResponseEntity<?> responseEntity = underTest.findAll();

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
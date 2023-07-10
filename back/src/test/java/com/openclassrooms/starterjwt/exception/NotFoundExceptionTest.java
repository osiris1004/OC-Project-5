package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {
    @Test
    public void testResponseStatus() {
        NotFoundException exception = new NotFoundException();
        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
        HttpStatus expectedHttpStatus = HttpStatus.NOT_FOUND;
        HttpStatus actualHttpStatus = responseStatus.value();

        assertEquals(expectedHttpStatus, actualHttpStatus);
    }

}
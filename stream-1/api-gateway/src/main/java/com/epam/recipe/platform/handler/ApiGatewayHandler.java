package com.epam.recipe.platform.handler;

import com.epam.recipe.platform.handler.exception.UnauthorizedException;
import com.epam.recipe.platform.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiGatewayHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Response> unauthorizedException(UnauthorizedException unauthorizedException){
        Response response = Response.builder()
                .errorMessage(unauthorizedException.getMessage())
                .statusCodeDescription(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<Response>(response, HttpStatus.UNAUTHORIZED);
    }

}

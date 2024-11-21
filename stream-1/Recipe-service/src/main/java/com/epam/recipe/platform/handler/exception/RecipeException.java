package com.epam.recipe.platform.handler.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


public class RecipeException extends RuntimeException{
    private final HttpStatus status;
        public RecipeException(String message, HttpStatus status){
            super(message);
            this.status = status;
        }

    public HttpStatus getStatus() {
        return status;
    }
}

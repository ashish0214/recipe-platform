package com.epam.recipe.platform.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private String errorMessage;
    private String statusCodeDescription;
    private LocalDateTime timeStamp;
}

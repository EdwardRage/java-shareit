package ru.practicum.shareit.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String error;
    private String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}

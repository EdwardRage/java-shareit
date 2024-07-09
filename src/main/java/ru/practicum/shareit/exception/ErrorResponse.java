package ru.practicum.shareit.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String description;

    public ErrorResponse(String description) {
        this.description = description;
    }
}

package ru.practicum.shareit.exception;

import lombok.Data;

@Data
public class ErrorResponseGetWay {
    private String error;
    private String description;
}

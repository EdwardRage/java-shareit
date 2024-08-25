package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}

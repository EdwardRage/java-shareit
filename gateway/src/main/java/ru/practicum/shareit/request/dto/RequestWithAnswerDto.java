package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestWithAnswerDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    //private ItemForRequestDto items;
    private List<ItemForRequestDto> items;

    @Data
    public static class ItemForRequestDto {
        private Long id;
        private String name;
        private String description;
    }
}

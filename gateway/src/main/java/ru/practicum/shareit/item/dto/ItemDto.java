package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class ItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingLastDto lastBooking;
    private BookingNextDto nextBooking;
    private List<CommentResponse> comments;

    @Data
    @AllArgsConstructor
    public static class BookingLastDto {
        private Long id;
        private Long bookerId;
    }

    @Data
    @AllArgsConstructor
    public static class BookingNextDto {
        private Long id;
        private Long bookerId;
    }
}

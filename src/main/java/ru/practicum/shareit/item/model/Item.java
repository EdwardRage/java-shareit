package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Item {
    @NotBlank
    private long id;
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private Boolean available;
    private Long owner;
    //private String request;
}

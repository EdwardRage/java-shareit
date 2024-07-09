package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class User {
    @NotBlank
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}

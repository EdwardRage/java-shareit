package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.get();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable long userId, @RequestBody User user) {
        return userService.update(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.delete(userId);
    }
}

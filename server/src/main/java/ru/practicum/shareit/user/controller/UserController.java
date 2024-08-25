package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        List<User> users = userService.get();
        log.info("get all users ==> " + users);
        return users;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        User user = userService.getUserById(userId);
        log.info("Get user by id ==> " + user);
        return user;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        User newUser = userService.create(user);
        log.info("Create new user ==> " + newUser);
        return newUser;
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable long userId, @RequestBody User user) {
        User updateUser = userService.update(userId, user);
        log.info("Update user ==> " + updateUser);
        return updateUser;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("Delete user by id ==> " + userId);
        userService.delete(userId);
    }
}

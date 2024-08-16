package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User update(long userId, User user);

    List<User> get();

    User getUserById(long id);

    void delete(long id);
}

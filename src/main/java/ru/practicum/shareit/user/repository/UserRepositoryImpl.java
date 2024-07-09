package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    private long id = 0;
    @Override
    public User create(User user) {
        user.setId(generatedId());
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> get() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> getUserById(long id) {
        try {
            User user = userMap.get(id);
            return Optional.ofNullable(user);
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(long id) {
        userMap.remove(id);
    }

    private long generatedId() {
        id++;
        return id;
    }
}

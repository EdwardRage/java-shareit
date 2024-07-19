package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        validateForCreate(user);
        return userRepository.create(user);
    }

    @Override
    public User update(long userId, User user) {
        User oldUser = getUserById(userId);
        if (user.getEmail() != null) {
            validateForUpdate(userId, user);
            oldUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        return userRepository.update(oldUser);
    }

    @Override
    public List<User> get() {
        return userRepository.get();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    @Override
    public void delete(long id) {
        User user = getUserById(id);
        userRepository.delete(user.getId());
    }

    private void validateForCreate(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Электронная почта не может быть пустой и не содержать символ @");
        }
        for (User u : get()) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new DuplicatedDataException("Электронная почта не может повторяться");
            }
        }
    }

    private void validateForUpdate(long userId, User user) {
        if (!user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Электронная почта не может не содержать символ @");
        }
        for (User u : get()) {
            if (u.getEmail().equals(user.getEmail()) && u.getId() != userId) {
                throw new DuplicatedDataException("Электронная почта не может повторяться");
            }
        }
    }
}

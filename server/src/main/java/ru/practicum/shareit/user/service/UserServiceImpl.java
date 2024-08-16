package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        return userRepository.save(user);
    }

    @Override
    public User update(long userId, User user) {
        User oldUser = getUserById(userId);
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        return userRepository.save(oldUser);
    }

    @Override
    public List<User> get() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    @Override
    public void delete(long id) {
        User user = getUserById(id);
        userRepository.deleteById(user.getId());
    }

    /*private void validateForCreate(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Электронная почта не может быть пустой и не содержать символ @");
        }
    }

    private void validateForUpdate(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Электронная почта не может не содержать символ @");
        }
    }*/
}
package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserById_found() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@test.com");
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertEquals(user, result);
    }

    @Test
    void createUserWithoutEmail() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("");
        Mockito.when(userRepository.save(user)).thenThrow(new ConditionsNotMetException("Имейл не может быть пустым"));

        final ConditionsNotMetException exception = Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> userService.create(user)
        );

        Assertions.assertEquals("Имейл не может быть пустым", exception.getMessage());
    }

    @Test
    void createUserIncorrectEmail() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test.com");
        Mockito.when(userRepository.save(user)).thenThrow(new ConditionsNotMetException("Имейл должен содержать символ @"));

        final ConditionsNotMetException exception = Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> userService.create(user)
        );

        Assertions.assertEquals("Имейл должен содержать символ @", exception.getMessage());
    }
}

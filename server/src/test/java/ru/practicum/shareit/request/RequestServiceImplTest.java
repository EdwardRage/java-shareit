package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.RequestIncomingDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    private User user;
    private RequestIncomingDto requestIn;
    private Request request;
    private RequestResponseDto requestResp;

    @BeforeEach
    void setUpEntity() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@test.tu");

        requestIn = new RequestIncomingDto();
        requestIn.setDescription("request description test");

        request = new Request();
        request.setId(1L);
        request.setRequestor(user);
        request.setDescription("request description test");
        request.setCreated(LocalDateTime.now());

        requestResp = new RequestResponseDto();
        requestResp.setId(1L);
        requestResp.setDescription("request description test");
        requestResp.setCreated(LocalDateTime.now());
    }

    @Test
    void createRequestTest() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        Mockito.when(requestMapper.mapToRequest(requestIn, user)).thenReturn(request);
        Mockito.when(requestRepository.save(request)).thenReturn(request);
        Mockito.when(requestMapper.mapToRequestResponseDto(request)).thenReturn(requestResp);

        RequestResponseDto requestResult = requestService.create(requestIn, 1L);

        assertEquals(requestResp, requestResult);
    }
}

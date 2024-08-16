package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestIncomingDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestWithAnswerDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public RequestResponseDto create(RequestIncomingDto requestIn, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Request request = requestMapper.mapToRequest(requestIn, user);
        requestRepository.save(request);
        return requestMapper.mapToRequestResponseDto(request);
    }

    @Override
    public Request update(Request request) {
        return null;
    }

    @Override
    public List<RequestWithAnswerDto> get(long requestorId) {
        List<RequestWithAnswerDto> requestDto = new ArrayList<>();
        List<Request> requests = requestRepository.findByRequestorId(requestorId);
        for (Request request : requests) {
            List<Item> answersItems = itemRepository.findByRequestId(request.getId());
            requestDto.add(requestMapper.mapToRequestWithAnswerDto(request, answersItems));
        }
        return requestDto;
    }

    @Override
    public RequestWithAnswerDto getById(long requestId, long userId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + requestId + " не найден"));
        List<Item> itemAnswers = itemRepository.findByRequestId(requestId);

        return requestMapper.mapToRequestWithAnswerDto(request, itemAnswers);
    }
}

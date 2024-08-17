package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestIncomingDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestWithAnswerDto;

import java.util.List;

public interface RequestService {

    RequestResponseDto create(RequestIncomingDto request, long userId);

    Request update(Request request);

    List<RequestWithAnswerDto> get(long requestorId);

    RequestWithAnswerDto getById(long requestId, long userId);
}

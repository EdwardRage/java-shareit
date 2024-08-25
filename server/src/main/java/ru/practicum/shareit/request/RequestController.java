package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestIncomingDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestWithAnswerDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestResponseDto create(@RequestBody RequestIncomingDto request,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        RequestResponseDto requestDto = requestService.create(request, userId);
        log.info("Create request ==> " + requestDto);
        return requestDto;
    }

    @GetMapping
    public List<RequestWithAnswerDto> get(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<RequestWithAnswerDto> requestDto = requestService.get(userId);
        log.info("Get request for user ==> " + requestDto);
        return requestDto;
    }

    @GetMapping("/{requestId}")
    public RequestWithAnswerDto getById(@PathVariable long requestId,
                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        RequestWithAnswerDto requestDto = requestService.getById(requestId, userId);
        log.info("get request by id ==> " + requestDto);
        return requestDto;
    }
}


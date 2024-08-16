package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestIncomingDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody RequestIncomingDto request,
                                         @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.create(request, userId);
    }

    @GetMapping
    public ResponseEntity<Object> get(@NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.get(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@NotNull @PathVariable long requestId,
                                          @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getById(requestId, userId);
    }
}

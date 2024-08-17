package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestIncomingDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestWithAnswerDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RequestMapper {

    public Request mapToRequest(RequestIncomingDto requestIn, User user) {
        Request request = new Request();
        request.setDescription(requestIn.getDescription());
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        return request;
    }

    public RequestResponseDto mapToRequestResponseDto(Request request) {
        RequestResponseDto requestDto = new RequestResponseDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        return requestDto;
    }

    public RequestWithAnswerDto mapToRequestWithAnswerDto(Request request, List<Item> items) {
        RequestWithAnswerDto requestDto = new RequestWithAnswerDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        List<RequestWithAnswerDto.ItemForRequestDto> itemDto = new ArrayList<>();
        for (Item item : items) {
            //requestDto.setItems(mapToItemForRequestDto(item));
            itemDto.add(mapToItemForRequestDto(item));
        }
        requestDto.setItems(itemDto);
        /*List<RequestWithAnswerDto> itemDto = items.stream().map(mapToItemForRequestDto(items))
        requestDto.setItem();*/
        return requestDto;
    }

    private RequestWithAnswerDto.ItemForRequestDto mapToItemForRequestDto(Item item) {
        RequestWithAnswerDto.ItemForRequestDto itemDto = new RequestWithAnswerDto.ItemForRequestDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        return itemDto;
    }
}

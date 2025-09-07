package ru.practicum.shareit.request.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;

/**
 * TODO Sprint add-item-requests.
 * Контроллер для работы с запросами вещей.
 * Обеспечивает REST API для управления информацией о запросах вещей.
 * Поддерживает операции:
 * - CRUD операции с запросами вещей
 * Все методы работают с сущностью {@link ItemRequest} и используют {@link RequestService} для бизнес-логики.
 */

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
}
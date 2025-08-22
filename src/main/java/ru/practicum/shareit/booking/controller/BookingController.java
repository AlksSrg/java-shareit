package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

/**
 * Контроллер для работы с бронированием вещей.
 * Обеспечивает REST API для управления информацией о бронировании вещей.
 * Поддерживает операции:
 * - CRUD операции с бронированием вещей
 * Все методы работают с сущностью {@link Booking} и используют {@link BookingService} для бизнес-логики.
 */

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
}
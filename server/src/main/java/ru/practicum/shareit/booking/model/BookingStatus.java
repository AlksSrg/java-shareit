package ru.practicum.shareit.booking.model;

/**
 * Перечисление возможных статусов бронирования.
 * WAITING — новое бронирование, ожидает одобрения
 * APPROVED — бронирование подтверждено владельцем
 * REJECTED — бронирование отклонено владельцем
 * CANCELED — бронирование отменено создателем
 * ALL — специальное значение для получения всех статусов (используется в фильтрах)
 */
public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED,
    ALL
}
package ru.practicum.shareit.exception.booking;

public class BookingNotFound extends RuntimeException {
    public BookingNotFound(String message) {
        super(message);
    }
}
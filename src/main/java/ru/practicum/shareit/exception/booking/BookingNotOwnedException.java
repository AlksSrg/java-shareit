package ru.practicum.shareit.exception.booking;

public class BookingNotOwnedException extends RuntimeException {
    public BookingNotOwnedException(String message) {
        super(message);
    }
}
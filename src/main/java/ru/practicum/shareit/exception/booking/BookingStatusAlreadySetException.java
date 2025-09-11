package ru.practicum.shareit.exception.booking;

public class BookingStatusAlreadySetException extends RuntimeException {
    public BookingStatusAlreadySetException(String message) {
        super(message);
    }
}
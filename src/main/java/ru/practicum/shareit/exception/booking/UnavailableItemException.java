package ru.practicum.shareit.exception.booking;

public class UnavailableItemException extends RuntimeException {
    public UnavailableItemException(String message) {
        super(message);
    }
}
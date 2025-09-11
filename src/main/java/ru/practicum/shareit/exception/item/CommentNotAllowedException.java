package ru.practicum.shareit.exception.item;

public class CommentNotAllowedException extends RuntimeException {
    public CommentNotAllowedException(String message) {
        super(message);
    }
}
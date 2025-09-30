package ru.practicum.shareit.user.dto;

/**
 * Базовый DTO для работы с пользователем на сервере.
 * Содержит основные поля пользователя без идентификатора.
 */

public record UserBaseDto(String name, String email) {
}
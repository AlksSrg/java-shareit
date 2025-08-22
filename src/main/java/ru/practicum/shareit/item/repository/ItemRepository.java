package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с вещами.
 * Определяет методы для CRUD операций и поиска вещей.
 */
public interface ItemRepository {

    /**
     * Сохраняет вещь.
     *
     * @param item вещь для сохранения
     * @return сохраненная вещь
     */
    Item save(Item item);

    /**
     * Находит вещь по идентификатору.
     *
     * @param id идентификатор вещи
     * @return Optional с найденной вещью или empty если не найдена
     */
    Optional<Item> findById(Long id);

    /**
     * Находит все вещи владельца.
     *
     * @param ownerId идентификатор владельца
     * @return список вещей владельца
     */
    List<Item> findByOwnerId(Long ownerId);

    /**
     * Находит вещи по идентификатору запроса.
     *
     * @param requestId идентификатор запроса
     * @return список вещей созданных по запросу
     */
    List<Item> findByRequestId(Long requestId);

    /**
     * Удаляет вещь по идентификатору.
     *
     * @param id идентификатор вещи для удаления
     */
    void deleteById(Long id);

    /**
     * Проверяет существование вещи по идентификатору.
     *
     * @param id идентификатор вещи
     * @return true если вещь существует, false в противном случае
     */
    boolean existsById(Long id);

    /**
     * Ищет доступные вещи по тексту в названии или описании.
     *
     * @param text текст для поиска
     * @return список найденных вещей
     */
    List<Item> searchAvailableItems(String text);
}
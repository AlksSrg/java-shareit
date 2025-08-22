package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.item.ItemNotFound;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Реализация репозитория вещей на основе HashMap.
 * Использует in-memory хранилище для демонстрационных целей.
 */
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    /**
     * Сохраняет вещь в репозитории.
     * Генерирует идентификатор если он не установлен.
     *
     * @param item вещь для сохранения
     * @return сохраненная вещь
     */
    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(idCounter.getAndIncrement());
        }
        items.put(item.getId(), item);
        return item;
    }

    /**
     * Находит вещь по идентификатору.
     *
     * @param id идентификатор вещи
     * @return Optional с найденной вещью
     */
    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    /**
     * Находит все вещи владельца.
     *
     * @param ownerId идентификатор владельца
     * @return список вещей владельца
     */
    @Override
    public List<Item> findByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner() != null && item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    /**
     * Находит вещи по идентификатору запроса.
     *
     * @param requestId идентификатор запроса
     * @return список вещей созданных по запросу
     */
    @Override
    public List<Item> findByRequestId(Long requestId) {
        return items.values().stream()
                .filter(item -> item.getRequestId() != null && item.getRequestId().equals(requestId))
                .collect(Collectors.toList());
    }

    /**
     * Удаляет вещь по идентификатору.
     *
     * @param id идентификатор вещи для удаления
     * @throws ItemNotFound если вещь не найдена
     */
    @Override
    public void deleteById(Long id) {
        if (!items.containsKey(id)) {
            throw new ItemNotFound("Вещь с id=" + id + " не найдена");
        }
        items.remove(id);
    }

    /**
     * Проверяет существование вещи по идентификатору.
     *
     * @param id идентификатор вещи
     * @return true если вещь существует
     */
    @Override
    public boolean existsById(Long id) {
        return items.containsKey(id);
    }

    /**
     * Ищет доступные вещи по тексту в названии или описании.
     *
     * @param text текст для поиска
     * @return список найденных вещей
     */
    @Override
    public List<Item> searchAvailableItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String searchText = text.toLowerCase().trim();
        return items.values().stream()
                .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                .filter(item ->
                        (item.getName() != null && item.getName().toLowerCase().contains(searchText)) ||
                                (item.getDescription() != null && item.getDescription().toLowerCase().contains(searchText))
                )
                .collect(Collectors.toList());
    }
}
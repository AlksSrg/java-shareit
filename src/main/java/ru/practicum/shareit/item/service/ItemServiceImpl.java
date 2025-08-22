package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.item.ItemNotFound;
import ru.practicum.shareit.exception.item.ItemNotOwnedByUserException;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * Реализация сервиса для работы с вещами.
 * Обеспечивает бизнес-логику операций CRUD и поиска вещей.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    /**
     * Создает новую вещь.
     *
     * @param ownerId              идентификатор владельца вещи
     * @param itemCreateRequestDto DTO с данными для создания вещи
     * @return созданная вещь в формате DTO
     */
    @Override
    public ItemResponseDto create(Long ownerId, ItemCreateRequestDto itemCreateRequestDto) {
        log.info("Создание вещи для пользователя {}: {}", ownerId, itemCreateRequestDto);
        // Проверяем существование владельца
        userService.getUserOrThrow(ownerId);

        Item item = itemMapper.toEntity(itemCreateRequestDto);
        item.setOwner(userService.getUserOrThrow(ownerId));
        Item savedItem = itemRepository.save(item);
        return itemMapper.toResponseDto(savedItem);
    }

    /**
     * Обновляет существующую вещь.
     *
     * @param id                   идентификатор вещи
     * @param ownerId              идентификатор владельца вещи
     * @param itemUpdateRequestDto DTO с данными для обновления
     * @return обновленная вещь в формате DTO
     */
    @Override
    public ItemResponseDto update(Long id, Long ownerId, ItemUpdateRequestDto itemUpdateRequestDto) {
        Item item = getItemOrThrow(id);

        // Проверяем, что вещь принадлежит пользователю
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ItemNotOwnedByUserException("Вещь с id=" + id + " не найдена у пользователя");
        }

        // Сохраняем оригинальные значения
        String originalName = item.getName();
        String originalDescription = item.getDescription();
        Boolean originalAvailable = item.getAvailable();

        itemMapper.updateEntity(itemUpdateRequestDto, item);

        // Восстанавливаем null значения
        if (item.getName() == null) item.setName(originalName);
        if (item.getDescription() == null) item.setDescription(originalDescription);
        if (item.getAvailable() == null) item.setAvailable(originalAvailable);

        Item updatedItem = itemRepository.save(item);
        return itemMapper.toResponseDto(updatedItem);
    }

    /**
     * Удаляет вещь по идентификатору.
     *
     * @param id      идентификатор вещи
     * @param ownerId идентификатор владельца вещи
     */
    @Override
    public void delete(Long id, Long ownerId) {
        Item item = getItemOrThrow(id);

        // Проверяем, что вещь принадлежит пользователю
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ItemNotOwnedByUserException("Вещь с id=" + id + " не принадлежит пользователю с id=" + ownerId);
        }

        itemRepository.deleteById(id);
    }

    /**
     * Находит вещь по идентификатору.
     *
     * @param id идентификатор вещи
     * @return вещь в формате DTO
     */
    @Override
    public ItemResponseDto findById(Long id) {
        Item item = getItemOrThrow(id);
        return itemMapper.toResponseDto(item);
    }

    /**
     * Находит вещь по идентификатору или выбрасывает исключение если не найдена.
     *
     * @param id идентификатор вещи
     * @return сущность вещи
     */
    @Override
    public Item getItemOrThrow(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFound("Вещь с id=" + id + " не найдена"));
    }

    /**
     * Находит все вещи владельца.
     *
     * @param ownerId идентификатор владельца
     * @return список вещей владельца
     */
    @Override
    public List<ItemResponseDto> findByOwnerId(Long ownerId) {
        // Проверяем существование пользователя
        userService.getUserOrThrow(ownerId);

        return itemRepository.findByOwnerId(ownerId).stream()
                .map(itemMapper::toResponseDto)
                .toList();
    }

    /**
     * Находит вещи по идентификатору запроса.
     *
     * @param requestId идентификатор запроса
     * @return список вещей созданных по запросу
     */
    @Override
    public List<ItemResponseDto> findByRequestId(Long requestId) {
        return itemRepository.findByRequestId(requestId).stream()
                .map(itemMapper::toResponseDto)
                .toList();
    }

    /**
     * Ищет доступные вещи по тексту в названии или описании.
     *
     * @param text текст для поиска
     * @return список найденных вещей
     */
    @Override
    public List<ItemResponseDto> searchAvailableItems(String text) {
        log.info("Поиск вещей по тексту: '{}'", text);
        List<Item> foundItems = itemRepository.searchAvailableItems(text);
        log.info("Найдено {} вещей", foundItems.size());
        log.debug("Найденные вещи: {}", foundItems);

        return foundItems.stream()
                .map(itemMapper::toResponseDto)
                .toList();
    }
}
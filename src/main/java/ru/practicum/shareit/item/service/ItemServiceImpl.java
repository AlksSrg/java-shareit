package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.item.ItemNotFound;
import ru.practicum.shareit.exception.item.ItemNotOwnedByUserException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с вещами.
 * Обеспечивает бизнес-логику операций CRUD и поиска вещей.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public void delete(Long id, Long ownerId) {
        Item item = getItemOrThrow(id);

        // Проверяем, что вещь принадлежит пользователю
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ItemNotOwnedByUserException("Вещь с id=" + id + " не принадлежит пользователю с id=" + ownerId);
        }

        itemRepository.deleteById(id);
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
     * @param from начальная позиция для пагинации
     * @param size количество элементов на странице
     * @return список найденных вещей
     */
    @Override
    public List<ItemResponseDto> searchAvailableItems(String text, Integer from, Integer size) {
        log.info("Поиск вещей по тексту: '{}' с пагинацией from={}, size={}", text, from, size);

        // Если текст пустой или null, возвращаем пустой список
        if (text == null || text.trim().isEmpty()) {
            log.info("Пустой текст поиска, возвращаем пустой список");
            return List.of();
        }

        List<Item> foundItems = itemRepository.searchAvailableItems(text.trim());
        log.info("Найдено {} вещей", foundItems.size());

        // Применяем пагинацию
        return foundItems.stream()
                .skip(from)
                .limit(size)
                .map(itemMapper::toResponseDto)
                .toList();
    }

    /**
     * Добавляет комментарий к вещи.
     *
     * @param itemId                  идентификатор вещи
     * @param userId                  идентификатор пользователя
     * @param commentCreateRequestDto DTO с данными для создания комментария
     * @return созданный комментарий в формате DTO
     */
    @Override
    @Transactional
    public CommentResponseDto addComment(Long itemId, Long userId, CommentCreateRequestDto commentCreateRequestDto) {
        log.info("Добавление комментария к вещи {} пользователем {}", itemId, userId);

        // Проверяем существование пользователя и вещи
        User author = userService.getUserOrThrow(userId);
        Item item = getItemOrThrow(itemId);

        // Проверяем, что пользователь брал вещь в аренду и аренда завершена
        if (!commentRepository.existsByUserAndItemWithCompletedBooking(userId, itemId)) {
            throw new IllegalArgumentException("Пользователь не брал вещь в аренду или аренда не завершена");
        }

        Comment comment = itemMapper.toCommentEntity(commentCreateRequestDto);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return itemMapper.toCommentResponseDto(savedComment);
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
        ItemResponseDto response = itemMapper.toResponseDto(item);

        List<CommentResponseDto> comments = getCommentsForItem(id);
        response.setComments(comments != null ? comments : Collections.emptyList());

        return response;
    }

    /**
     * Находит все вещи владельца с пагинацией.
     *
     * @param ownerId идентификатор владельца
     * @param from    начальная позиция
     * @param size    количество элементов
     * @return список вещей владельца
     */
    @Override
    public List<ItemResponseDto> findByOwnerId(Long ownerId, Integer from, Integer size) {
        log.info("Поиск вещей пользователя {} с пагинацией from={}, size={}", ownerId, from, size);

        // Проверяем существование пользователя
        userService.getUserOrThrow(ownerId);

        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        log.info("Найдено {} вещей пользователя {}", items.size(), ownerId);

        // Применяем пагинацию
        return items.stream()
                .skip(from)
                .limit(size)
                .map(itemMapper::toResponseDto)
                .toList();
    }

    /**
     * Находит все комментарии для вещи
     *
     * @param itemId идентификатор вещи
     * @return список комментариев (never null)
     */
    private List<CommentResponseDto> getCommentsForItem(Long itemId) {
        List<Comment> comments = commentRepository.findByItemId(itemId);

        // Гарантируем, что никогда не возвращаем null
        if (comments == null) {
            log.warn("CommentRepository.findByItemId({}) returned null", itemId);
            return Collections.emptyList();
        }

        return comments.stream()
                .map(itemMapper::toCommentResponseDto)
                .collect(Collectors.toList());
    }
}
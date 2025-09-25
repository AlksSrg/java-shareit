package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.item.CommentNotAllowedException;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.item.ItemNotOwnedByUserException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
    private final ItemRequestRepository itemRequestRepository;

    /**
     * Создает новую вещь.
     *
     * @param ownerId              идентификатор владельца вещи
     * @param itemCreateRequestDto DTO с данными для создания вещи
     * @return DTO созданной вещи
     */
    @Override
    @Transactional
    public ItemResponseDto create(Long ownerId, ItemCreateRequestDto itemCreateRequestDto) {
        log.info("Создание вещи для пользователя {}: {}", ownerId, itemCreateRequestDto);

        User owner = userService.getUserOrThrow(ownerId);
        Item item = itemMapper.toEntity(itemCreateRequestDto);
        item.setOwner(owner);

        if (itemCreateRequestDto.requestId() != null) {
            Long requestId = itemCreateRequestDto.requestId();
            if (!itemRequestRepository.existsById(requestId)) {
                throw new NotFoundException("Запрос с ID " + requestId + " не найден");
            }
            item.setRequestId(requestId);
        }

        Item savedItem = itemRepository.save(item);
        ItemResponseDto responseDto = itemMapper.toResponseDto(savedItem);

        // Добавляем комментарии к ответу
        List<CommentResponseDto> comments = getCommentsForItem(savedItem.getId());
        return new ItemResponseDto(
                responseDto.id(),
                responseDto.name(),
                responseDto.description(),
                responseDto.available(),
                responseDto.owner(),
                responseDto.requestId(),
                comments,
                responseDto.lastBooking(),
                responseDto.nextBooking()
        );
    }

    /**
     * Обновляет существующую вещь.
     *
     * @param id                   идентификатор вещи для обновления
     * @param ownerId              идентификатор владельца вещи
     * @param itemUpdateRequestDto DTO с данными для обновления вещи
     * @return DTO обновленной вещи
     */
    @Override
    @Transactional
    public ItemResponseDto update(Long id, Long ownerId, ItemUpdateRequestDto itemUpdateRequestDto) {
        Item item = getItemOrThrow(id);

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ItemNotOwnedByUserException("Вещь с id=" + id + " не найдена у пользователя");
        }

        itemMapper.updateEntity(itemUpdateRequestDto, item);
        Item updatedItem = itemRepository.save(item);
        ItemResponseDto responseDto = itemMapper.toResponseDto(updatedItem);

        // Добавляем комментарии к ответу
        List<CommentResponseDto> comments = getCommentsForItem(updatedItem.getId());
        return new ItemResponseDto(
                responseDto.id(),
                responseDto.name(),
                responseDto.description(),
                responseDto.available(),
                responseDto.owner(),
                responseDto.requestId(),
                comments,
                responseDto.lastBooking(),
                responseDto.nextBooking()
        );
    }

    /**
     * Удаляет вещь по идентификатору.
     *
     * @param id      идентификатор вещи для удаления
     * @param ownerId идентификатор владельца вещи
     */
    @Override
    @Transactional
    public void delete(Long id, Long ownerId) {
        Item item = getItemOrThrow(id);

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
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id=" + id + " не найдена"));
    }

    /**
     * Находит вещи по идентификатору запроса.
     *
     * @param requestId идентификатор запроса
     * @return список DTO вещей, связанных с запросом
     */
    @Override
    public List<ItemResponseDto> findByRequestId(Long requestId) {
        return itemRepository.findByRequestId(requestId).stream()
                .map(item -> {
                    ItemResponseDto responseDto = itemMapper.toResponseDto(item);
                    List<CommentResponseDto> comments = getCommentsForItem(item.getId());
                    return new ItemResponseDto(
                            responseDto.id(),
                            responseDto.name(),
                            responseDto.description(),
                            responseDto.available(),
                            responseDto.owner(),
                            responseDto.requestId(),
                            comments,
                            responseDto.lastBooking(),
                            responseDto.nextBooking()
                    );
                })
                .toList();
    }

    /**
     * Ищет доступные вещи по тексту в названии или описании.
     *
     * @param text текст для поиска
     * @param from начальная позиция пагинации
     * @param size количество элементов на странице
     * @return список DTO найденных вещей
     */
    @Override
    public List<ItemResponseDto> searchAvailableItems(String text, Integer from, Integer size) {
        log.info("Поиск вещей по тексту: '{}' с пагинацией from={}, size={}", text, from, size);

        if (text == null || text.trim().isEmpty()) {
            log.info("Пустой текст поиска, возвращаем пустой список");
            return Collections.emptyList();
        }

        List<Item> foundItems = itemRepository.searchAvailableItems(text.trim());
        log.info("Найдено {} вещей", foundItems.size());

        return foundItems.stream()
                .skip(from)
                .limit(size)
                .map(item -> {
                    ItemResponseDto responseDto = itemMapper.toResponseDto(item);
                    List<CommentResponseDto> comments = getCommentsForItem(item.getId());
                    return new ItemResponseDto(
                            responseDto.id(),
                            responseDto.name(),
                            responseDto.description(),
                            responseDto.available(),
                            responseDto.owner(),
                            responseDto.requestId(),
                            comments,
                            responseDto.lastBooking(),
                            responseDto.nextBooking()
                    );
                })
                .toList();
    }

    /**
     * Добавляет комментарий к вещи.
     *
     * @param itemId                  идентификатор вещи
     * @param userId                  идентификатор пользователя
     * @param commentCreateRequestDto DTO с данными комментария
     * @return DTO созданного комментария
     */
    @Override
    @Transactional
    public CommentResponseDto addComment(Long itemId, Long userId, CommentCreateRequestDto commentCreateRequestDto) {
        log.info("Добавление комментария к вещи {} пользователем {}", itemId, userId);

        User author = userService.getUserOrThrow(userId);
        Item item = getItemOrThrow(itemId);

        if (!commentRepository.hasUserBookedItemWithCompletedBooking(userId, itemId)) {
            log.warn("Пользователь {} не имеет завершенного бронирования вещи {}", userId, itemId);
            throw new CommentNotAllowedException("Пользователь не брал вещь в аренду или аренда не завершена");
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
     * @return DTO вещи с комментариями
     */
    @Override
    public ItemResponseDto findById(Long id) {
        Item item = getItemOrThrow(id);
        ItemResponseDto responseDto = itemMapper.toResponseDto(item);
        List<CommentResponseDto> comments = getCommentsForItem(id);
        return new ItemResponseDto(
                responseDto.id(),
                responseDto.name(),
                responseDto.description(),
                responseDto.available(),
                responseDto.owner(),
                responseDto.requestId(),
                comments,
                responseDto.lastBooking(),
                responseDto.nextBooking()
        );
    }

    /**
     * Находит все вещи владельца с пагинацией.
     *
     * @param ownerId идентификатор владельца
     * @param from    начальная позиция пагинации
     * @param size    количество элементов на странице
     * @return список DTO вещей владельца
     */
    @Override
    public List<ItemResponseDto> findByOwnerId(Long ownerId, Integer from, Integer size) {
        log.info("Поиск вещей пользователя {} с пагинацией from={}, size={}", ownerId, from, size);

        userService.getUserOrThrow(ownerId);
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        log.info("Найдено {} вещей пользователя {}", items.size(), ownerId);

        return items.stream()
                .skip(from)
                .limit(size)
                .map(item -> {
                    ItemResponseDto responseDto = itemMapper.toResponseDto(item);
                    List<CommentResponseDto> comments = getCommentsForItem(item.getId());
                    return new ItemResponseDto(
                            responseDto.id(),
                            responseDto.name(),
                            responseDto.description(),
                            responseDto.available(),
                            responseDto.owner(),
                            responseDto.requestId(),
                            comments,
                            responseDto.lastBooking(),
                            responseDto.nextBooking()
                    );
                })
                .toList();
    }

    /**
     * Находит все комментарии для вещи.
     *
     * @param itemId идентификатор вещи
     * @return список DTO комментариев для вещи
     */
    private List<CommentResponseDto> getCommentsForItem(Long itemId) {
        List<Comment> comments = commentRepository.findByItemId(itemId);
        return comments != null ? comments.stream()
                .map(itemMapper::toCommentResponseDto)
                .toList() : Collections.emptyList();
    }
}
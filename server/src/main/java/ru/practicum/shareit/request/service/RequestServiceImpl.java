package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Реализация сервиса для работы с запросами вещей.
 * Обрабатывает бизнес-логику создания и получения запросов вещей.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    /**
     * Создает новый запрос вещи.
     *
     * @param itemRequestDto DTO запроса вещи.
     * @param userId         идентификатор пользователя, создающего запрос.
     * @return DTO созданного запроса вещи с присвоенным идентификатор и временем создания
     * @throws NotFoundException если пользователь с указанным идентификатором не найден
     */
    @Override
    @Transactional
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        log.info("Создание запроса вещи для пользователя {}", userId);

        User user = getUserOrThrow(userId);
        ItemRequest itemRequest = itemRequestMapper.toEntity(itemRequestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequest savedRequest = itemRequestRepository.save(itemRequest);
        return enrichWithItems(savedRequest);
    }

    /**
     * Получает список всех запросов вещей, созданных указанным пользователем.
     *
     * @param userId идентификатор пользователя, для которого получаются запросы.
     * @return список DTO запросов вещей пользователя, отсортированный по дате создания (от новых к старым)
     * @throws NotFoundException если пользователь с указанным идентификатором не найден
     */
    @Override
    public List<ItemRequestDto> getOwnItemRequests(Long userId) {
        log.info("Получение собственных запросов пользователя {}", userId);

        getUserOrThrow(userId);
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);

        return requests.stream()
                .map(this::enrichWithItems)
                .toList();
    }

    /**
     * Получает список запросов вещей других пользователей с поддержкой пагинации.
     *
     * @param userId идентификатор пользователя, который делает запрос.
     * @param from   начальная позиция пагинации
     * @param size   количество элементов на странице
     * @return список DTO запросов вещей других пользователей, отсортированный по дате создания (от новых к старым)
     * @throws NotFoundException        если пользователь с указанным идентификатором не найден
     * @throws IllegalArgumentException если параметры пагинации невалидны
     */
    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        log.info("Получение запросов других пользователей для userId={}, from={}, size={}", userId, from, size);

        getUserOrThrow(userId);
        validatePaginationParams(from, size);

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"));
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdNot(userId, pageRequest);

        return requests != null ? requests.stream()
                .map(this::enrichWithItems)
                .toList() : Collections.emptyList();
    }

    /**
     * Получает конкретный запрос вещи по его идентификатору.
     *
     * @param requestId идентификатор запроса вещи.
     * @param userId    идентификатор пользователя, который делает запрос.
     * @return DTO запроса вещи с информацией о связанных вещах
     * @throws NotFoundException если пользователь или запрос с указанным идентификатором не найден
     */
    @Override
    public ItemRequestDto getItemRequestById(Long requestId, Long userId) {
        log.info("Получение запроса по ID {} для пользователя {}", requestId, userId);

        getUserOrThrow(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID " + requestId + " не найден"));

        return enrichWithItems(itemRequest);
    }

    /**
     * Обогащает DTO запроса информацией о вещах, связанных с этим запросом.
     *
     * @param itemRequest сущность запроса
     * @return DTO запроса с дополнительной информацией о связанных вещах
     */
    private ItemRequestDto enrichWithItems(ItemRequest itemRequest) {
        List<ItemResponseDto> items = itemRepository.findByRequestId(itemRequest.getId()).stream()
                .map(itemMapper::toResponseDto)
                .toList();

        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items != null ? items : Collections.emptyList())
                .build();
    }

    /**
     * Получает пользователя или выбрасывает исключение если не найден.
     *
     * @param userId идентификатор пользователя
     * @return найденный пользователь
     * @throws NotFoundException если пользователь с указанным идентификатором не найден
     */
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
    }

    /**
     * Проверяет параметры пагинации на валидность.
     *
     * @param from индекс первого элемента
     * @param size количество элементов
     * @throws IllegalArgumentException если параметры пагинации невалидны
     */
    private void validatePaginationParams(Integer from, Integer size) {
        if (from == null || from < 0) {
            throw new IllegalArgumentException("Параметр 'from' должен быть неотрицательным числом");
        }
        if (size == null || size <= 0) {
            throw new IllegalArgumentException("Параметр 'size' должен быть положительным числом");
        }
    }
}
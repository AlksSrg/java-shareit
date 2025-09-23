package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.booking.BookingNotFoundException;
import ru.practicum.shareit.exception.booking.BookingNotOwnedException;
import ru.practicum.shareit.exception.booking.BookingStatusAlreadySetException;
import ru.practicum.shareit.exception.booking.UnavailableItemException;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.user.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

/**
 * Реализация сервиса для работы с бронированиями.
 * Обрабатывает бизнес-логику создания, обновления и поиска бронирований.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    /**
     * Создает новое бронирование.
     *
     * @param userId                  идентификатор пользователя создающего бронирование
     * @param bookingCreateRequestDto DTO с данными для создания бронирования
     * @return созданное бронирование в формате DTO
     */
    @Override
    @Transactional
    public BookingResponseDto create(Long userId, BookingCreateRequestDto bookingCreateRequestDto) {
        log.info("Создание бронирования для пользователя {}: {}", userId, bookingCreateRequestDto);

        User booker = getUserOrThrow(userId);
        Item item = getItemOrThrow(bookingCreateRequestDto.itemId());

        validateBookingCreation(item, userId);

        Booking booking = bookingMapper.toEntity(bookingCreateRequestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toResponseDto(savedBooking);
    }

    /**
     * Обновляет статус бронирования (подтверждение/отклонение).
     *
     * @param userId    идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @param approved  флаг подтверждения (true - подтверждено, false - отклонено)
     * @return обновленное бронирование в format DTO
     */
    @Override
    @Transactional
    public BookingResponseDto updateStatus(Long userId, Long bookingId, Boolean approved) {
        log.info("Обновление статуса бронирования {} пользователем {} на {}", bookingId, userId, approved);

        Booking booking = getBookingOrThrow(bookingId);
        validateBookingOwnership(booking, userId);
        validateBookingStatus(booking);

        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newStatus);

        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toResponseDto(updatedBooking);
    }

    /**
     * Находит бронирование по идентификатору.
     *
     * @param userId    идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @return найденное бронирование в формате DTO
     */
    @Override
    public BookingResponseDto findById(Long userId, Long bookingId) {
        log.info("Поиск бронирования {} пользователем {}", bookingId, userId);

        Booking booking = getBookingOrThrow(bookingId);
        validateBookingAccess(booking, userId);

        return bookingMapper.toResponseDto(booking);
    }

    /**
     * Находит все бронирования пользователя с фильтрацией по статусу.
     *
     * @param bookerId идентификатор пользователя-арендатора
     * @param status   статус бронирования для фильтрации
     * @param from     начальный индекс для пагинации
     * @param size     количество элементов на странице
     * @return список бронирований пользователя
     */
    @Override
    public List<BookingResponseDto> findByBookerId(Long bookerId, BookingStatus status, Integer from, Integer size) {
        log.info("Поиск бронирований пользователя {} со статусом {}", bookerId, status);

        getUserOrThrow(bookerId);
        Pageable pageable = createPageRequest(from, size);

        List<Booking> bookings = getBookingsByBooker(bookerId, status, pageable);
        return mapBookingsToDto(bookings);
    }

    /**
     * Находит все бронирования для вещей владельца с фильтрацией по статусу.
     *
     * @param ownerId идентификатор владельца вещей
     * @param status  статус бронирования для фильтрации
     * @param from    начальный индекс для пагинации
     * @param size    количество элементов на странице
     * @return список бронирований для вещей владельца
     */
    @Override
    public List<BookingResponseDto> findByOwnerId(Long ownerId, BookingStatus status, Integer from, Integer size) {
        log.info("Поиск бронирований владельца {} со статусом {}", ownerId, status);

        getUserOrThrow(ownerId);
        Pageable pageable = createPageRequest(from, size);

        List<Booking> bookings = getBookingsByOwner(ownerId, status, pageable);
        return mapBookingsToDto(bookings);
    }

    /**
     * Получает бронирование по идентификатору или выбрасывает исключение если не найдено.
     *
     * @param bookingId идентификатор бронирования
     * @return найденное бронирование
     */
    @Override
    public Booking getBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование с id=" + bookingId + " не найдено"));
    }

    /**
     * Создает объект пагинации.
     *
     * @param from начальный индекс
     * @param size количество элементов
     * @return объект пагинации
     */
    private Pageable createPageRequest(Integer from, Integer size) {
        return PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
    }

    /**
     * Преобразует список бронирований в DTO.
     *
     * @param bookings список бронирований
     * @return список DTO
     */
    private List<BookingResponseDto> mapBookingsToDto(List<Booking> bookings) {
        return bookings.stream()
                .map(bookingMapper::toResponseDto)
                .toList();
    }

    /**
     * Получает бронирования по арендатору с учетом статуса.
     *
     * @param bookerId идентификатор арендатора
     * @param status   статус бронирования
     * @param pageable параметры пагинации
     * @return список бронирований
     */
    private List<Booking> getBookingsByBooker(Long bookerId, BookingStatus status, Pageable pageable) {
        if (status == null || status == BookingStatus.ALL) {
            return bookingRepository.findByBookerId(bookerId, pageable);
        }

        return switch (status) {
            case WAITING, APPROVED, REJECTED, CANCELED ->
                    bookingRepository.findByBookerIdAndStatus(bookerId, status, pageable);
            default -> bookingRepository.findByBookerId(bookerId, pageable);
        };
    }

    /**
     * Получает бронирования по владельцу с учетом статуса.
     *
     * @param ownerId  идентификатор владельца
     * @param status   статус бронирования
     * @param pageable параметры пагинации
     * @return список бронирований
     */
    private List<Booking> getBookingsByOwner(Long ownerId, BookingStatus status, Pageable pageable) {
        if (status == null || status == BookingStatus.ALL) {
            return bookingRepository.findByItemOwnerId(ownerId, pageable);
        }

        return switch (status) {
            case WAITING, APPROVED, REJECTED, CANCELED ->
                    bookingRepository.findByItemOwnerIdAndStatus(ownerId, status, pageable);
            default -> bookingRepository.findByItemOwnerId(ownerId, pageable);
        };
    }

    /**
     * Проверяет возможность создания бронирования.
     *
     * @param item   вещь для бронирования
     * @param userId идентификатор пользователя
     */
    private void validateBookingCreation(Item item, Long userId) {
        if (!item.getAvailable()) {
            throw new UnavailableItemException("Вещь с id=" + item.getId() + " недоступна для бронирования");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new BookingNotOwnedException("Владелец не может бронировать свою вещь");
        }
    }

    /**
     * Проверяет, что пользователь является владельцем вещи.
     *
     * @param booking бронирование для проверки
     * @param userId  идентификатор пользователя
     */
    private void validateBookingOwnership(Booking booking, Long userId) {
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new BookingNotOwnedException("Только владелец вещи может подтверждать бронирование");
        }
    }

    /**
     * Проверяет, что статус бронирования можно изменить.
     *
     * @param booking бронирование для проверки
     */
    private void validateBookingStatus(Booking booking) {
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new BookingStatusAlreadySetException("Статус бронирования уже установлен");
        }
    }

    /**
     * Проверяет доступ пользователя к просмотру бронирования.
     *
     * @param booking бронирование для проверки
     * @param userId  идентификатор пользователя
     */
    private void validateBookingAccess(Booking booking, Long userId) {
        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new BookingNotOwnedException("Просмотр бронирования доступен только автору или владельцу вещи");
        }
    }

    /**
     * Получает пользователя по идентификатору или выбрасывает исключение если не найден.
     *
     * @param userId идентификатор пользователя
     * @return найденный пользователь
     */
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    /**
     * Получает вещь по идентификатору или выбрасывает исключение если не найдена.
     *
     * @param itemId идентификатор вещи
     * @return найденная вещь
     */
    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id=" + itemId + " не найдена"));
    }
}
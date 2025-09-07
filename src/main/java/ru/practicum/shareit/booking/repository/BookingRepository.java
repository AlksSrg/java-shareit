package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с бронированиями.
 * Определяет методы для CRUD операций и поиска бронирований.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Находит все бронирования пользователя с пагинацией.
     *
     * @param bookerId идентификатор пользователя
     * @param pageable параметры пагинации
     * @return список бронирований пользователя
     */
    List<Booking> findByBookerId(Long bookerId, Pageable pageable);

    /**
     * Находит все бронирования пользователя с указанным статусом с пагинацией.
     *
     * @param bookerId идентификатор пользователя
     * @param status   статус бронирования
     * @param pageable параметры пагинации
     * @return список бронирований пользователя с указанным статусом
     */
    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    /**
     * Находит все бронирования для вещей владельца с пагинацией.
     *
     * @param ownerId  идентификатор владельца вещей
     * @param pageable параметры пагинации
     * @return список бронирований для вещей владельца
     */
    List<Booking> findByItemOwnerId(Long ownerId, Pageable pageable);

    /**
     * Находит все бронирования для вещей владельца с указанным статусом с пагинацией.
     *
     * @param ownerId  идентификатор владельца вещей
     * @param status   статус бронирования
     * @param pageable параметры пагинации
     * @return список бронирований для вещей владельца с указанным статусом
     */
    List<Booking> findByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Pageable pageable);

    /**
     * Находит все бронирования пользователя отсортированные по дате начала (по убыванию).
     *
     * @param bookerId идентификатор пользователя
     * @return список бронирований пользователя
     */
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    /**
     * Находит все бронирования пользователя с указанным статусом отсортированные по дате начала (по убыванию).
     *
     * @param bookerId идентификатор пользователя
     * @param status   статус бронирования
     * @return список бронирований пользователя с указанным статусом
     */
    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    /**
     * Находит текущие бронирования пользователя (начались, но еще не закончились).
     *
     * @param bookerId идентификатор пользователя
     * @param start    время начала для фильтрации
     * @param end      время окончания для фильтрации
     * @return список текущих бронирований пользователя
     */
    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end);

    /**
     * Находит завершенные бронирования пользователя.
     *
     * @param bookerId идентификатор пользователя
     * @param end      время окончания для фильтрации
     * @return список завершенных бронирований пользователя
     */
    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    /**
     * Находит будущие бронирования пользователя.
     *
     * @param bookerId идентификатор пользователя
     * @param start    время начала для фильтрации
     * @return список будущих бронирований пользователя
     */
    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    /**
     * Находит все бронирования для вещей владельца отсортированные по дате начала (по убыванию).
     *
     * @param ownerId идентификатор владельца вещей
     * @return список бронирований для вещей владельца
     */
    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    /**
     * Находит все бронирования для вещей владельца с указанным статусом отсортированные по дате начала (по убыванию).
     *
     * @param ownerId идентификатор владельца вещей
     * @param status  статус бронирования
     * @return список бронирований для вечей владельца с указанным статусом
     */
    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    /**
     * Находит текущие бронирования для вещей владельца (начались, но еще не закончились).
     *
     * @param ownerId идентификатор владельца вещей
     * @param start   время начала для фильтрации
     * @param end     время окончания для фильтрации
     * @return список текущих бронирований для вещей владельца
     */
    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId, LocalDateTime start, LocalDateTime end);

    /**
     * Находит завершенные бронирования для вещей владельца.
     *
     * @param ownerId идентификатор владельца вещей
     * @param end     время окончания для фильтрации
     * @return список завершенных бронирований для вещей владельца
     */
    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime end);

    /**
     * Находит будущие бронирования для вещей владельца.
     *
     * @param ownerId идентификатор владельца вещей
     * @param start   время начала для фильтрации
     * @return список будущих бронирований для вещей владельца
     */
    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime start);

    /**
     * Находит все бронирования вещи с указанными статусами отсортированные по дате начала (по возрастанию).
     *
     * @param itemId   идентификатор вещи
     * @param statuses список статусов бронирования
     * @return список бронирований вещи с указанными статусами
     */
    List<Booking> findByItemIdAndStatusInOrderByStartAsc(
            Long itemId, List<BookingStatus> statuses);

    /**
     * Проверяет существование завершенного бронирования пользователя для вещи.
     *
     * @param bookerId идентификатор пользователя
     * @param itemId   идентификатор вещи
     * @param status   статус бронирования
     * @param end      время окончания для фильтрации
     * @return true если бронирование существует
     */
    boolean existsByBookerIdAndItemIdAndStatusAndEndBefore(
            Long bookerId, Long itemId, BookingStatus status, LocalDateTime end);

    /**
     * Находит последнее завершенное бронирование для вещи.
     *
     * @param itemId идентификатор вещи
     * @param now    текущее время для фильтрации
     * @return список последних бронирований
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.status = 'APPROVED' " +
            "AND b.end < :now " +
            "ORDER BY b.end DESC")
    List<Booking> findLastBookingsForItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    /**
     * Находит следующее бронирование для вещи.
     *
     * @param itemId идентификатор вещи
     * @param now    текущее время для фильтрации
     * @return список следующих бронирований
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.status = 'APPROVED' " +
            "AND b.start > :now " +
            "ORDER BY b.start ASC")
    List<Booking> findNextBookingsForItem(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    /**
     * Находит последние завершенные бронирования для списка вещей.
     *
     * @param itemIds список идентификаторов вещей
     * @param now     текущее время для фильтрации
     * @return список последних бронирований
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id IN :itemIds " +
            "AND b.status = 'APPROVED' " +
            "AND b.end < :now " +
            "AND b.id IN (SELECT MAX(b2.id) FROM Booking b2 WHERE b2.item.id = b.item.id AND b2.end < :now GROUP BY b2.item.id)")
    List<Booking> findLastBookingsForItems(@Param("itemIds") List<Long> itemIds, @Param("now") LocalDateTime now);

    /**
     * Находит следующие бронирования для списка вещей.
     *
     * @param itemIds список идентификаторов вещей
     * @param now     текущее время для фильтрации
     * @return список следующих бронирований
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id IN :itemIds " +
            "AND b.status = 'APPROVED' " +
            "AND b.start > :now " +
            "AND b.id IN (SELECT MIN(b2.id) FROM Booking b2 WHERE b2.item.id = b.item.id AND b2.start > :now GROUP BY b2.item.id)")
    List<Booking> findNextBookingsForItems(@Param("itemIds") List<Long> itemIds, @Param("now") LocalDateTime now);

    /**
     * Находит последнее завершенное бронирование для вещи (удобный метод).
     *
     * @param itemId идентификатор вещи
     * @return Optional с последним бронированием
     */
    default Optional<Booking> findLastBookingForItem(Long itemId) {
        List<Booking> bookings = findLastBookingsForItem(itemId, LocalDateTime.now());
        return bookings.isEmpty() ? Optional.empty() : Optional.of(bookings.get(0));
    }

    /**
     * Находит следующее бронирование для вещи (удобный метод).
     *
     * @param itemId идентификатор вещи
     * @return Optional со следующим бронированием
     */
    default Optional<Booking> findNextBookingForItem(Long itemId) {
        List<Booking> bookings = findNextBookingsForItem(itemId, LocalDateTime.now());
        return bookings.isEmpty() ? Optional.empty() : Optional.of(bookings.get(0));
    }

    /**
     * Находит последние завершенные бронирования для списка вещей (удобный метод).
     *
     * @param itemIds список идентификаторов вещей
     * @return список последних бронирований
     */
    default List<Booking> findLastBookingsForItems(List<Long> itemIds) {
        return findLastBookingsForItems(itemIds, LocalDateTime.now());
    }

    /**
     * Находит следующие бронирования для списка вещей (удобный метод).
     *
     * @param itemIds список идентификаторов вещей
     * @return список следующих бронирований
     */
    default List<Booking> findNextBookingsForItems(List<Long> itemIds) {
        return findNextBookingsForItems(itemIds, LocalDateTime.now());
    }
}
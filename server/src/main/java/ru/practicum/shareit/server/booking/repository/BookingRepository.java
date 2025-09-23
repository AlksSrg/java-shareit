package ru.practicum.shareit.server.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.BookingStatus;

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
     * Находит текущие бронирования пользователя (начались, но еще не закончились).
     *
     * @param bookerId идентификатор пользователя
     * @param now      текущее время для фильтрации
     * @return список текущих бронирований пользователя
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start <= :now " +
            "AND b.end >= :now " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByBookerId(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    /**
     * Находит завершенные бронирования пользователя.
     *
     * @param bookerId идентификатор пользователя
     * @param now      текущее время для фильтрации
     * @return список завершенных бронирований пользователя
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.end < :now " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByBookerId(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    /**
     * Находит будущие бронирования пользователя.
     *
     * @param bookerId идентификатор пользователя
     * @param now      текущее время для фильтрации
     * @return список будущих бронирований пользователя
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start > :now " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureBookingsByBookerId(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    /**
     * Находит текущие бронирования для вещей владельца (начались, но еще не закончились).
     *
     * @param ownerId идентификатор владельца вещей
     * @param now     текущее время для фильтрации
     * @return список текущих бронирований для вещей владельца
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start <= :now " +
            "AND b.end >= :now " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByOwnerId(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    /**
     * Находит завершенные бронирования для вещей владельца.
     *
     * @param ownerId идентификатор владельца вещей
     * @param now     текущее время для фильтрации
     * @return список завершенных бронирований для вещей владельца
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.end < :now " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByOwnerId(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    /**
     * Находит будущие бронирования для вещей владельца.
     *
     * @param ownerId идентификатор владельца вещей
     * @param now     текущее время для фильтрации
     * @return список будущих бронирований для вещей владельца
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start > :now " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureBookingsByOwnerId(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

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
     * Находит последнее завершенное бронирование для вещи.
     *
     * @param itemId идентификатор вещи
     * @return Optional с последним бронированием
     */
    default Optional<Booking> findLastBookingForItem(Long itemId) {
        List<Booking> bookings = findLastBookingsForItem(itemId, LocalDateTime.now());
        return bookings.isEmpty() ? Optional.empty() : Optional.of(bookings.getFirst());
    }

    /**
     * Находит следующее бронирование для вещи.
     *
     * @param itemId идентификатор вещи
     * @return Optional со следующим бронированием
     */
    default Optional<Booking> findNextBookingForItem(Long itemId) {
        List<Booking> bookings = findNextBookingsForItem(itemId, LocalDateTime.now());
        return bookings.isEmpty() ? Optional.empty() : Optional.of(bookings.getFirst());
    }

    /**
     * Находит последние завершенные бронирования для списка вещей.
     *
     * @param itemIds список идентификаторов вещей
     * @return список последних бронирований
     */
    default List<Booking> findLastBookingsForItems(List<Long> itemIds) {
        return findLastBookingsForItems(itemIds, LocalDateTime.now());
    }

    /**
     * Находит следующие бронирования для списка вещей.
     *
     * @param itemIds список идентификаторов вещей
     * @return список следующих бронирований
     */
    default List<Booking> findNextBookingsForItems(List<Long> itemIds) {
        return findNextBookingsForItems(itemIds, LocalDateTime.now());
    }
}
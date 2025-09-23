package ru.practicum.shareit.server.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.server.item.model.Comment;

import java.util.List;

/**
 * Репозиторий для работы с отзывами.
 * Определяет методы для CRUD операций и поиска комментариев.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Находит все отзывы для вещи.
     *
     * @param itemId идентификатор вещи
     * @return список отзывов
     */
    List<Comment> findByItemId(Long itemId);

    /**
     * Находит все отзывы для списка вещей.
     *
     * @param itemIds список идентификаторов вещей
     * @return список отзывов
     */
    List<Comment> findByItemIdIn(List<Long> itemIds);

    /**
     * Проверяет, брал ли пользователь вещь в аренду.
     *
     * @param userId идентификатор пользователя
     * @param itemId идентификатор вещи
     * @return true если пользователь брал вещь в аренду
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.booker.id = :userId " +
            "AND b.item.id = :itemId " +
            "AND b.status = 'APPROVED' " +
            "AND b.end < CURRENT_TIMESTAMP")
    boolean hasUserBookedItemWithCompletedBooking(@Param("userId") Long userId,
                                                  @Param("itemId") Long itemId);
}
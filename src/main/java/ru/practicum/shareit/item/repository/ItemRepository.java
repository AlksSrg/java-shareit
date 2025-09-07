package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Репозиторий для работы с вещами.
 * Определяет методы для CRUD операций и поиска вещей.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

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
     * Ищет доступные вещи по тексту в названии или описании.
     *
     * @param text текст для поиска
     * @return список найденных вещей
     */
    @Query("SELECT i FROM Item i " +
            "JOIN FETCH i.owner " +
            "WHERE i.available = true " +
            "AND (:text IS NULL OR :text = '' OR " +
            "(LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%'))))")
    List<Item> searchAvailableItems(@Param("text") String text);

    /**
     * Проверяет существование вещи по идентификатору владельца.
     *
     * @param itemId  идентификатор вещи
     * @param ownerId идентификатор владельца
     * @return true если вещь принадлежит владельцу
     */
    boolean existsByIdAndOwnerId(Long itemId, Long ownerId);
}
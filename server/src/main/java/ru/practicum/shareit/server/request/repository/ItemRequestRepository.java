package ru.practicum.shareit.server.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.server.request.model.ItemRequest;

import java.util.List;

/**
 * Репозиторий для работы с запросами вещей.
 * Определяет методы для CRUD операций и поиска запросов.
 */
@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    /**
     * Находит запросы пользователя, отсортированные по дате создания (новые сначала).
     *
     * @param requestorId идентификатор пользователя
     * @return список запросов
     */
    List<ItemRequest> findByRequestorIdOrderByCreatedDesc(Long requestorId);

    /**
     * Находит запросы других пользователей, отсортированные по дате создания (новые сначала).
     *
     * @param requestorId идентификатор пользователя для исключения
     * @param pageable    параметры пагинации
     * @return список запросов
     */
    List<ItemRequest> findByRequestorIdNot(Long requestorId, Pageable pageable);

    /**
     * Проверяет существование запроса по идентификатору.
     *
     * @param requestId идентификатор запроса
     * @return true если запрос существует
     */
    boolean existsById(Long requestId);
}
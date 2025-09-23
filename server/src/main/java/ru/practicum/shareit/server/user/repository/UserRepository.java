package ru.practicum.shareit.server.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.server.user.model.User;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * Предоставляет методы доступа к данным пользователей.
 * Наследует стандартные CRUD операции от JpaRepository.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по email.
     *
     * @param email email для поиска
     * @return Optional с пользователем, если найден
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(@Param("email") String email);

    /**
     * Проверяет существование пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return true если пользователь существует, false в противном случае
     */
    boolean existsById(Long id);
}
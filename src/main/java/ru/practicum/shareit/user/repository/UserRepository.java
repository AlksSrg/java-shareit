package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * Определяет методы для CRUD операций и поиска пользователей.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Проверяет существование пользователя по email.
     *
     * @param email email пользователя
     * @return true если пользователь с таким email существует
     */
    boolean existsByEmail(String email);

    /**
     * Находит пользователя по email.
     *
     * @param email email пользователя
     * @return Optional с найденным пользователем
     */
    Optional<User> findByEmail(String email);

    /**
     * Находит пользователя по email игнорируя регистр.
     *
     * @param email email пользователя
     * @return Optional с найденным пользователем
     */
    Optional<User> findByEmailIgnoreCase(String email);
}
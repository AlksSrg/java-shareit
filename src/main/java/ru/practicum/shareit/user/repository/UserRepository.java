package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * Определяет методы для CRUD операций и поиска пользователей.
 */
public interface UserRepository {

    /**
     * Сохраняет пользователя.
     *
     * @param user пользователь для сохранения
     * @return сохраненный пользователь
     */
    User save(User user);

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return Optional с найденным пользователем или empty если не найден
     */
    Optional<User> findById(Long id);

    /**
     * Находит всех пользователей.
     *
     * @return список всех пользователей
     */
    List<User> findAll();

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя для удаления
     */
    void deleteById(Long id);

    /**
     * Проверяет существование пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return true если пользователь существует, false в противном случае
     */
    boolean existsById(Long id);

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
}
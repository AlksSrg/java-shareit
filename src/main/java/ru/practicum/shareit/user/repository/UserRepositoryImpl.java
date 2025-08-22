package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.user.UserNotFound;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Реализация репозитория пользователей на основе HashMap.
 * Использует in-memory хранилище для демонстрационных целей.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    /**
     * Сохраняет пользователя в репозитории.
     * Генерирует идентификатор если он не установлен.
     *
     * @param user пользователь для сохранения
     * @return сохраненный пользователь
     */
    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return Optional с найденным пользователем
     */
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    /**
     * Находит всех пользователей.
     *
     * @return список всех пользователей
     */
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя для удаления
     * @throws UserNotFound если пользователь не найден
     */
    @Override
    public void deleteById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFound("Пользователь с id=" + id + " не найден");
        }
        users.remove(id);
    }

    /**
     * Проверяет существование пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return true если пользователь существует
     */
    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    /**
     * Проверяет существование пользователя по email.
     *
     * @param email email пользователя
     * @return true если пользователь с таким email существует
     */
    @Override
    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    /**
     * Находит пользователя по email.
     *
     * @param email email пользователя
     * @return Optional с найденным пользователем
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst();
    }
}
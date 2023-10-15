package ru.ylab.repository;

import ru.ylab.model.User;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    /**
     * Найдет пользователя, которому принадлежит данный счет.
     * @param walletId
     * @return
     */
    Optional<User> findByWalletId(Long walletId);
}

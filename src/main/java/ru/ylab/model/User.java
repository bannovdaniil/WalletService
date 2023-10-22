package ru.ylab.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Пользователь - он же игрок
 * firstName - имя
 * lastName - фамилия
 * hashPassword - хеш пароля игрока
 * wallet - счет игрока
 */
@Getter
@Setter()
public class User {
    private final Long id;
    @Setter(AccessLevel.NONE)
    private final String firstName;
    @Setter(AccessLevel.NONE)
    private final String lastName;
    @Setter(AccessLevel.NONE)
    private final String hashPassword;
    private Wallet wallet;

    public User(Long id, String firstName, String lastName, String hashPassword, Wallet wallet) {
        this.id = id;
        this.hashPassword = hashPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return String.format("User{id = %d, FirstName = %s, LastName = %s, %s}",
                id,
                firstName,
                lastName,
                wallet);
    }
}

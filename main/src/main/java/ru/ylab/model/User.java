package ru.ylab.model;

/**
 * Пользователь - он же игрок
 * firstName - имя
 * lastName - фамилия
 * hashPassword - хеш пароля игрока
 * wallet - счет игрока
 */
public class User {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private String hashPassword;
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

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}

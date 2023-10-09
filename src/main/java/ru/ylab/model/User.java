package ru.ylab.model;

/**
 * Пользователь - он же игрок
 * firstName - имя
 * lastName - фамилия
 * wallet - счет игрока
 */
public class User extends Entity {
    private String firstName;
    private String lastName;
    private String hashPassword;
    private Wallet wallet;

    public User(Long id, String firstName, String lastName, String hashPassword, Wallet wallet) {
        this.hashPassword = hashPassword;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.wallet = wallet;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return String.format("User: %nid= %d %nFirst name= %s %nLastName= %s %nPassword Hash= %s",
                id,
                firstName,
                lastName,
                stringToHex(hashPassword));
    }

    private String stringToHex(String hashPassword) {
        StringBuilder sb = new StringBuilder();
        for (char c : hashPassword.toCharArray()) {
            sb.append(String.format("%04x", (int) c)).append(" ");
        }
        return sb.toString();
    }
}

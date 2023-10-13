package ru.ylab.repository.impl;

import ru.ylab.model.Wallet;
import ru.ylab.repository.WalletRepository;

public final class WalletRepositoryImpl extends RepositoryImpl<Wallet> implements WalletRepository {

    private static WalletRepository instance;

    public static synchronized WalletRepository getInstance() {
        if (instance == null) {
            instance = new WalletRepositoryImpl();
        }
        return instance;
    }

}

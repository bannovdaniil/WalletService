package ru.ylab.in.ui.menu.action;

import ru.ylab.in.ui.Session;
import ru.ylab.in.ui.SessionImpl;
import ru.ylab.model.Wallet;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.WalletRepositoryImpl;

import java.text.NumberFormat;

public class WalletShowBalance implements ItemAction {
    private final Session session = SessionImpl.getInstance();
    private final WalletRepository walletRepository = WalletRepositoryImpl.getInstance();

    @Override
    public void execution() {
        if (session.isPresent()) {
            Long walletId = session.getUserWallet().getId();
            Wallet wallet = walletRepository.findById(walletId).orElseThrow(
                    () -> new IllegalStateException("Wallet Not found.")
            );
            System.out.println("You Balance: " + NumberFormat.getCurrencyInstance().format(wallet.getBalance()));
        }
    }

}

package ru.ylab.in.ui.menu.action;

import ru.ylab.in.ui.Session;
import ru.ylab.in.ui.SessionImpl;
import ru.ylab.model.Wallet;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.WalletRepositoryImpl;

import java.text.NumberFormat;
import java.util.Optional;

public class WalletShowBalance implements ItemAction {
    private final Session session = SessionImpl.getInstance();
    private final WalletRepository walletRepository = WalletRepositoryImpl.getInstance();

    @Override
    public void execution() {
        Long walletId = session.getUserWallet().orElseThrow(
                () -> new IllegalStateException("Wallet Not found.")
        ).getId();
        Optional<Wallet> wallet = walletRepository.findById(walletId);
        if (wallet.isPresent()) {
            System.out.println("You Balance: " + NumberFormat.getCurrencyInstance().format(wallet.get().getBalance()));
        } else {
            System.err.println("Счет не активирован.");
        }

    }
}

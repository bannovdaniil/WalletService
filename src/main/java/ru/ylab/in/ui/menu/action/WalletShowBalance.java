package ru.ylab.in.ui.menu.action;

import ru.ylab.exception.NotFoundException;
import ru.ylab.in.ui.Session;
import ru.ylab.in.ui.SessionImpl;
import ru.ylab.model.Wallet;
import ru.ylab.service.WalletService;
import ru.ylab.service.impl.WalletServiceImpl;

import java.text.NumberFormat;

/**
 * {@inheritDoc}
 * Показать баланс.
 */
public class WalletShowBalance implements ItemAction {
    private final Session session = SessionImpl.getInstance();
    private final WalletService walletService = WalletServiceImpl.getInstance();

    @Override
    public void execution() {
        if (session.isPresent()) {
            Long walletId = session.getUserWallet().getId();
            try {
                Wallet wallet = walletService.findById(walletId);
                System.out.println("You Balance: " + NumberFormat.getCurrencyInstance().format(wallet.getBalance()));
            } catch (NotFoundException e) {
                System.err.println("Wallet Not found.");
            }
        }
    }

}

package ru.ylab.in.ui.menu.action;

import ru.ylab.in.ui.Session;
import ru.ylab.in.ui.SessionImpl;
import ru.ylab.model.Wallet;
import ru.ylab.service.WalletService;
import ru.ylab.service.impl.WalletServiceImpl;

import java.text.NumberFormat;
import java.util.Scanner;

/**
 * {@inheritDoc}
 * Кредит кошелька.
 */
public class WalletPutMoney implements ItemAction {
    private final Session session = SessionImpl.getInstance();
    private final WalletService walletService = WalletServiceImpl.getInstance();

    @Override
    public void execution() {
        if (session.isPresent()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter sum: ");
            String moneyValue = scanner.next();

            try {
                Long walletId = session.getUserWallet().getId();
                Wallet wallet = walletService.putMoney(walletId, moneyValue);
                session.setUserWallet(wallet);

                System.out.println("New balance: " + NumberFormat.getCurrencyInstance().format(wallet.getBalance()));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

    }
}
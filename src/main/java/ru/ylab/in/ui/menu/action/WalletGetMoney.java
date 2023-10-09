package ru.ylab.in.ui.menu.action;

import ru.ylab.in.ui.Session;
import ru.ylab.in.ui.SessionImpl;
import ru.ylab.model.Wallet;
import ru.ylab.repository.WalletRepository;
import ru.ylab.repository.impl.WalletRepositoryImpl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Scanner;

public class WalletGetMoney implements ItemAction {
    private String regexFormatMoney = "^\\d*([\\.,]\\d{1,2})?$";
    private final Session session = SessionImpl.getInstance();
    private final WalletRepository walletRepository = WalletRepositoryImpl.getInstance();

    @Override
    public void execution() {
        if (session.isPresent()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter sum: ");
            String moneyValue = scanner.next();

            if (moneyValue.matches(regexFormatMoney)) {
                try {
                    BigDecimal subtractValue = new BigDecimal(moneyValue.replace(",", "."));

                    Long walletId = session.getUserWallet().getId();
                    Wallet wallet = walletRepository.findById(walletId).orElseThrow(
                            () -> new IllegalStateException("Wallet Not found.")
                    );

                    if (wallet.getBalance().compareTo(subtractValue) < 0) {
                        throw new IllegalArgumentException("Не достаточно средств.");
                    }

                    if (wallet.getBalance().compareTo(subtractValue) > 0) {
                        wallet.setBalance(wallet.getBalance().subtract(subtractValue));
                        walletRepository.update(wallet);
                        wallet = walletRepository.findById(walletId).orElseThrow(
                                () -> new IllegalStateException("Wallet Not found.")
                        );
                        System.out.println("New balance: " + NumberFormat.getCurrencyInstance().format(wallet.getBalance()));
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else {
                System.err.println("Bad enter.");
            }
        }
    }
}

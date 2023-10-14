package ru.ylab;

import ru.ylab.in.ui.menu.Item;
import ru.ylab.in.ui.menu.ItemType;
import ru.ylab.in.ui.menu.Menu;
import ru.ylab.in.ui.menu.action.*;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.impl.LiquibaseUtilImpl;


/**
 * Wallet-Service.
 * Сервис, который управляет кредитными/дебетовыми транзакциями от имени игроков.
 * Инициализация меню.
 */
public class Main {
    public static void main(String[] args) {

        LiquibaseUtil util = LiquibaseUtilImpl.getInstance();
        util.init();

        Menu menu = new Menu();
        menu.addElement(ItemType.MAIN_MENU, new Item("Registration User", new UserRegistration()));
        menu.addElement(ItemType.MAIN_MENU, new Item("Login", new UserLogin()));

        menu.addElement(ItemType.LOGIN_USER_MENU, new Item("Show Balance", new WalletShowBalance()));
        menu.addElement(ItemType.LOGIN_USER_MENU, new Item("Put money", new WalletPutMoney()));
        menu.addElement(ItemType.LOGIN_USER_MENU, new Item("Get money", new WalletGetMoney()));
        menu.addElement(ItemType.LOGIN_USER_MENU, new Item("Show history of transaction", new TransactionHistory()));
        menu.addElement(ItemType.LOGIN_USER_MENU, new Item("Show User by ID", new UserFindById()));
        menu.addElement(ItemType.LOGIN_USER_MENU, new Item("Show All User", new UserFindAll()));
        menu.addElement(ItemType.LOGIN_USER_MENU, new Item("User audit logs", new UserAudit()));
        menu.addElement(ItemType.LOGIN_USER_MENU, new Item("Logout", new UserLogout()));

        menu.doAction();

    }
}
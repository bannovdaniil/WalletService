package ru.ylab;

import ru.ylab.in.ui.menu.Item;
import ru.ylab.in.ui.menu.Menu;
import ru.ylab.in.ui.menu.action.*;

public class Main {
    public static void main(String[] args) {

        Menu menu = new Menu();
        menu.addElement(new Item("Registration User", new UserRegistration()));
        menu.addElement(new Item("Show User by ID", new UserFindById()));
        menu.addElement(new Item("Show All User", new UserFindAll()));

        menu.addElement(new Item("Login", new UserLogin()));
        menu.addElement(new Item("Logout", new UserLogout()));

        menu.doAction();

    }
}
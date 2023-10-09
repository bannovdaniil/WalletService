package ru.ylab.util.menu.action;

import ru.ylab.util.menu.Session;
import ru.ylab.util.menu.SessionImpl;

public class UserLogout implements ItemAction {
    private Session session = SessionImpl.getInstance();

    @Override
    public void execution() {
        if (session.isPresent()) {
            session.logout();
        }
    }
}

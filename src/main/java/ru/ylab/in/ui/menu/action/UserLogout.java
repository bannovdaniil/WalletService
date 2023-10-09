package ru.ylab.in.ui.menu.action;

import ru.ylab.in.ui.Session;
import ru.ylab.in.ui.SessionImpl;

public class UserLogout implements ItemAction {
    private Session session = SessionImpl.getInstance();

    @Override
    public void execution() {
        if (session.isPresent()) {
            session.logout();
        }
    }
}

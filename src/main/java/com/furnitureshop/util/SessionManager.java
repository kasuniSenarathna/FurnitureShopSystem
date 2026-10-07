package com.furnitureshop.util;

import com.furnitureshop.model.User;

/** Singleton that remembers who is logged in. */
public class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();

    private User currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void clear() {
        this.currentUser = null;
    }
}

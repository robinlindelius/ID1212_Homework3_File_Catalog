package se.kth.id1212.server.model;

import se.kth.id1212.common.Listener;

/**
 * Created by Robin on 2017-12-03.
 */
public class UserHandler {
    private final User user;
    private final Listener notificationListener;

    public UserHandler(User user, Listener notificationListener) {
        this.user = user;
        this.notificationListener = notificationListener;
    }

    public User getUser() {
        return user;
    }

    public Listener getNotificationListener() {
        return notificationListener;
    }

    public long getUserID() {
        return user.getUserID();
    }

    public String getUsername() {
        return user.getUsername();
    }
}

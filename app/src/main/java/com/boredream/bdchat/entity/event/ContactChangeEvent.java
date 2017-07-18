package com.boredream.bdchat.entity.event;

import com.boredream.bdcodehelper.entity.User;

public class ContactChangeEvent {

    public static final int TYPE_ADD = 1;
    public static final int TYPE_CHANG = 2;

    private int type;
    private User user;

    public ContactChangeEvent(int type, User user) {
        this.type = type;
        this.user = user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

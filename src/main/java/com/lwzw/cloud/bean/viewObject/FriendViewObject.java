package com.lwzw.cloud.bean.viewObject;

import com.lwzw.cloud.bean.User;

public class FriendViewObject {
    private User user;
    private boolean isFriend;

    public FriendViewObject(User user, boolean isFriend) {
        this.user = user;
        this.isFriend = isFriend;
    }

    public FriendViewObject() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    @Override
    public String toString() {
        return "FriendViewObject{" +
                "user=" + user +
                ", isFriend=" + isFriend +
                '}';
    }
}

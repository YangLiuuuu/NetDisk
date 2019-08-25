package com.lwzw.cloud.bean.viewObject;

import com.lwzw.cloud.bean.Message;
import com.lwzw.cloud.bean.User;

public class MessageViewObject {
    private User fromUser;//发送者
    private User toUser;//接受者
    private Message message;//消息体
    private int isSender;//查询者是否是发送者

    public MessageViewObject(User fromUser, User toUser, Message message, int isSender) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
        this.isSender = isSender;
    }

    public MessageViewObject() {
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getIsSender() {
        return isSender;
    }

    public void setIsSender(int isSender) {
        this.isSender = isSender;
    }

    @Override
    public String toString() {
        return "MessageViewObject{" +
                "fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", message=" + message +
                ", isSender=" + isSender +
                '}';
    }
}


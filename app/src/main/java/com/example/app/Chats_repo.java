package com.example.app;

public class Chats_repo {
    private String message;
    private String sender;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Chats_repo(String message, String sender) {

        this.message = message;
        this.sender = sender;
    }
}
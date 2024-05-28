package com.example.app;

public class Message_repo {

    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";

    String message;
    String sentBy;

    public String getMessage_repo() {
        return message;
    }

    public void setMessage_repo(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public Message_repo(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }
}
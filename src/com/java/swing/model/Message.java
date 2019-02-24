package com.java.swing.model;

public class Message {

    private String title;
    private String contents;

    public Message(String message, String contents) {
        this.title = message;
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}

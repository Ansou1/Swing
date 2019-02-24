package com.java.swing.gui;

public class ServerInfo {

    private String name;
    private int id;
    private boolean checked;

    public ServerInfo(String name, int id, boolean checked) {
        this.name = name;
        this.id = id;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return name;
    }
}

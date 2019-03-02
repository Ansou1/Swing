package com.java.swing.model;

public enum EmploymentCategory {
    employed("employed"),
    selfEmployed("selfEmployed"),
    unemplyed("unemplyed"),
    other("other");

    private String text;

    EmploymentCategory(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return text;
    }
}

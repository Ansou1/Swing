package com.java.swing;

import com.java.swing.model.*;

import java.sql.SQLException;

public class TestDatabase {

    public static void main(String[] args) {
        System.out.println("Running Database test");

        Database db = new Database();
        try {
            db.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.addPeople(new Person("Simon", "Software engineer", AgeCategory.adult, EmploymentCategory.employed, "777", true, Gender.male));
        db.addPeople(new Person("NAPA", "Ikea HR", AgeCategory.adult, EmploymentCategory.employed, "666", true, Gender.female));

        try {
            db.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.load();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.disconnect();
    }
}

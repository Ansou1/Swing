package com.java.swing.controller;

import com.java.swing.gui.FormEvent;
import com.java.swing.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {
    Database db = new Database();

    public List<Person> getPeople() {
        return db.getPeople();
    }

    public void addPerson(FormEvent ev) {
        String name = ev.getName();
        String occupation = ev.getOccupation();
        int ageCat = ev.getAgeCategory();
        String empCat = ev.getEmpCat();
        Boolean isUS = ev.isUsCitizen();
        String taxId = ev.getTaxId();
        String gender = ev.getGender();

        AgeCategory ageCategory = AgeCategory.child;
        switch (ageCat) {
            case 0:
                ageCategory = AgeCategory.child;
                break;
            case 1:
                ageCategory = AgeCategory.adult;
                break;
            case 2:
                ageCategory = AgeCategory.senior;
                break;
        }

        EmploymentCategory employmentCategory;
        if (empCat.equals("employed"))
            employmentCategory = EmploymentCategory.employed;
        else if (empCat.equals("self-employed"))
            employmentCategory = EmploymentCategory.selfEmployed;
        else
            employmentCategory = EmploymentCategory.unemplyed;

        Gender gender1;
        if (gender.equals("male"))
            gender1 = Gender.male;
        else
            gender1 = Gender.female;


        Person person = new Person(name, occupation, ageCategory, employmentCategory, taxId, isUS, gender1);
        db.addPeople(person);
    }

    public void saveToFile(File file) throws IOException {
        db.saveToFile(file);
    }

    public void loadFromFile(File file) throws IOException {
        db.loadFromFile(file);
    }

    public void removePerson(int index) {
        db.removePerson(index);
    }
}

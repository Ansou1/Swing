package com.java.swing.model;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Database {
    private List<Person> people;
    Connection conn;

    public Database() {
        this.people = new LinkedList<>();
    }

    public void addPeople(Person person) {
        this.people.add(person);
    }

    public List<Person> getPeople() {
        return Collections.unmodifiableList(this.people);
    }

    public void saveToFile(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        Person[] persons = people.toArray(new Person[people.size()]);

        oos.writeObject(persons);

        oos.close();
    }

    public void loadFromFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        try {
            Person[] person = (Person[]) ois.readObject();
            people.clear();
            people.addAll(Arrays.asList(person));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ois.close();
    }

    public void removePerson(int index) {
        this.people.remove(index);
    }

    public void connect() throws Exception {
        if (conn != null) return;

        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:/Users/simondaguenet/IdeaProjects/Swing1/test.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (this.conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void load() throws SQLException {
        people.clear();

        String sql = "SELECT id, name, age, employment_status, tax_id, us_citizen, gender, occupation FROM people ORDER BY name";

        Statement selectStatement = conn.createStatement();

        ResultSet result = selectStatement.executeQuery(sql);

        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("name");
            String ageCategory = result.getString("age");
            String employmentCategory = result.getString("employment_status");
            String taxId = result.getString("tax_id");
            Boolean usCitizen = result.getBoolean("us_citizen");
            String gender = result.getString("gender");
            String occupation = result.getString("occupation");
            Person person = new Person(id, name, occupation, AgeCategory.valueOf(ageCategory), EmploymentCategory.valueOf(employmentCategory), taxId, usCitizen, Gender.valueOf(gender));
            people.add(person);
        }

        result.close();
        selectStatement.close();
    }

    public void save() throws SQLException {
        String checkSQL = "SELECT count(*) as count from people where id=?";

        PreparedStatement checkStmt = conn.prepareStatement(checkSQL);

        String insertSQL = "INSERT into people (id, name, age, employment_status, tax_id, us_citizen, gender, occupation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String updateSQL = "UPDATE people set name=?, age=?, employment_status=?, tax_id=?, us_citizen=?, gender=?, occupation=? where id=?";

        PreparedStatement insertStatement = conn.prepareStatement(insertSQL);
        PreparedStatement updateStatement = conn.prepareStatement(updateSQL);

        for (Person person : this.people) {
            int id = person.getId();
            String name = person.getName();
            String occupation = person.getOccupation();
            AgeCategory ageCategory = person.getAgeCategory();
            EmploymentCategory employmentCategory = person.getEmpCat();
            String taxId = person.getTaxId();
            Boolean usCitizen = person.isUsCitizen();
            Gender gender = person.getGender();

            checkStmt.setInt(1, id);

            ResultSet checkResult = checkStmt.executeQuery();
            checkResult.next();
            int count = checkResult.getInt(1);

            if (count == 0) {
                System.out.println("Insert person with ID " + id );
                insertStatement.setInt(1, id);
                insertStatement.setString(2, name);
                insertStatement.setString(3, ageCategory.toString());
                insertStatement.setString(4, employmentCategory.toString());
                insertStatement.setString(5, taxId);
                insertStatement.setBoolean(6, usCitizen);
                insertStatement.setString(7, gender.toString());
                insertStatement.setString(8, occupation);

                insertStatement.executeUpdate();
            } else {
                System.out.println("Update person with ID " + id );
                updateStatement.setString(1, name);
                updateStatement.setString(2, ageCategory.toString());
                updateStatement.setString(3, employmentCategory.toString());
                updateStatement.setString(4, taxId);
                updateStatement.setBoolean(5, usCitizen);
                updateStatement.setString(6, gender.toString());
                updateStatement.setString(7, occupation);
                updateStatement.setInt(8, id);

                updateStatement.executeUpdate();
            }
        }
        insertStatement.close();
        updateStatement.close();
        checkStmt.close();
    }
}

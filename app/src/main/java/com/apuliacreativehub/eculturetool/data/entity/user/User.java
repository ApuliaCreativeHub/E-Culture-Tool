package com.apuliacreativehub.eculturetool.data.entity.user;

public class User {
    private String name;
    private String surname;
    private String email;
    private String password;
    private boolean isACurator;

    public User(String name, String surname, String email, String password, boolean isACurator) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.isACurator = isACurator;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isACurator() {
        return isACurator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setACurator(boolean ACurator) {
        isACurator = ACurator;
    }

}
package com.apuliacreativehub.eculturetool.viewmodels;

import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {

    private String name = "";
    private String surname = "";
    private String email = "";
    private String password = "";
    private String confirmPassword = "";
    private boolean isCurator = Boolean.parseBoolean(null);

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public boolean getIsCurator() {
        return isCurator;
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

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setIsCurator(boolean isCurator) {
        this.isCurator = isCurator;
    }

}
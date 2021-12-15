package com.apuliacreativehub.eculturetool.viewmodels;

import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import java.util.regex.Pattern;

public class RegisterViewModel extends ViewModel {

    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Z][a-z]{2,14}");
    private static final Pattern SURNAME_PATTERN = Pattern.compile("[A-Z][a-z]{2,14}([ ][A-Z][a-z]{2,14})?");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");

    private String name = "";
    private String surname = "";
    private String email = "";
    private String password = "";
    private String confirmPassword = "";
    private boolean isCurator = Boolean.parseBoolean(null);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean checkName(String name) {
        boolean error = false;

        if(!NAME_PATTERN.matcher(name).matches())
            error = true;

        return error;
    }

    public boolean checkSurname(String surname) {
        boolean error = false;

        if(!SURNAME_PATTERN.matcher(surname).matches())
            error = true;

        return error;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean checkEmail(String email) {
        boolean error = false;

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            error = true;

        return error;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String password) {
        boolean error = false;

        if(!PASSWORD_PATTERN.matcher(password).matches())
            error = true;

        return error;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean checkConfirmPassword(String password, String confirmPassword) {
        boolean error = false;

        if(!password.equals(confirmPassword))
            error = true;

        return error;
    }

    public boolean getIsCurator() {
        return isCurator;
    }

    public void setIsCurator(boolean isCurator) {
        this.isCurator = isCurator;
    }

}
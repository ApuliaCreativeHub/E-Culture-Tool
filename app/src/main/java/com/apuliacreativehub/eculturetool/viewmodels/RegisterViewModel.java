package com.apuliacreativehub.eculturetool.viewmodels;

import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import java.util.regex.Pattern;

public class RegisterViewModel extends ViewModel {

    private static final int MIN_CHARACTERS_NAME = 3;
    private static final int MAX_CHARACTER_NAME = 50;
    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Z][a-z]*");

    private static final int MIN_CHARACTERS_SURNAME = 3;
    private static final int MAX_CHARACTER_SURNAME = 50;
    private static final Pattern SURNAME_PATTERN = Pattern.compile("[A-Z][a-z]*([ '-][A-Z][a-z]*)*");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +      // at least 1 digit
                    "(?=.*[a-z])" +      // at least 1 lower case letter
                    "(?=.*[A-Z])" +      // at least 1 upper case letter
                    "(?=.*[@#$%^&+=])" + // at least 1 special character
                    "(?=\\S+$)" +        // no white spaces
                    ".{6,}" +            // at least 8 characters
                    "$");

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

    public String checkName(String name) {
        String error = "";

        if(name.isEmpty())
            error = "empty";
        else if(name.length() < MIN_CHARACTERS_NAME || name.length() > MAX_CHARACTER_NAME)
            error = "name_too_long_or_short";
        else if(!NAME_PATTERN.matcher(name).matches())
            error = "invalid_name";

        return error;
    }

    public String checkSurname(String surname) {
        String error = "";

        if(surname.isEmpty())
            error = "empty";
        else if(surname.length() < MIN_CHARACTERS_SURNAME || surname.length() > MAX_CHARACTER_SURNAME)
            error = "surname_too_long_or_short";
        else if(!SURNAME_PATTERN.matcher(surname).matches())
            error = "invalid_surname";

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

    public String checkEmail(String email) {
        String error = "";

        if(email.isEmpty())
            error = "empty";
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            error = "invalid_email";

        return error;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String checkPassword(String password) {
        String error = "";

        if(password.isEmpty())
            error = "empty";
        else if(!PASSWORD_PATTERN.matcher(password).matches())
            error = "invalid_password";

        return error;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String checkConfirmPassword(String password, String confirmPassword) {
        String error = "";

        if(confirmPassword.isEmpty())
            error = "empty";
        else if(!password.equals(confirmPassword))
            error = "invalid_confirm_password";

        return error;
    }

    public boolean getIsCurator() {
        return isCurator;
    }

    public void setIsCurator(boolean isCurator) {
        this.isCurator = isCurator;
    }

}
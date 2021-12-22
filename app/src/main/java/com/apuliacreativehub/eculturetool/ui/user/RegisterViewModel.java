package com.apuliacreativehub.eculturetool.ui.user;

import android.app.Application;
import android.util.Patterns;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.NullLiveDataException;
import com.apuliacreativehub.eculturetool.data.entity.User;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.data.repository.UserRepository;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

import java.util.regex.Pattern;

public class RegisterViewModel extends AndroidViewModel {

    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Z][a-z]{2,14}");
    private static final Pattern SURNAME_PATTERN = Pattern.compile("[A-Z][a-z]{2,14}([ ][A-Z][a-z]{2,14})?");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");

    private String name = "";
    private String surname = "";
    private String email = "";
    private String password = "";
    private String confirmPassword = "";
    private boolean isCurator = Boolean.parseBoolean(null);
    private final UserRepository repository;

    private MutableLiveData<RepositoryNotification<String>> registrationResult;

    public <T> RegisterViewModel(Application application) {
        super(application);
        ECultureTool app = getApplication();
        this.repository = new UserRepository(app.executorService);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNameCorrect(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isSurnameCorrect(String surname) {
        return SURNAME_PATTERN.matcher(surname).matches();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailCorrect(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPasswordCorrect(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isConfirmPasswordCorrect(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public boolean getIsCurator() {
        return isCurator;
    }

    public void setIsCurator(boolean isCurator) {
        this.isCurator = isCurator;
    }

    public void registerUser() {
        User user = new User(name, surname, email, password, isCurator);
        registrationResult = repository.registerUser(user);
    }

    public MutableLiveData<RepositoryNotification<String>> getRegistrationResult() throws NullLiveDataException {
        if (registrationResult == null) {
            throw new NullLiveDataException();
        } else {
            return registrationResult;
        }
    }
}
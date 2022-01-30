package com.apuliacreativehub.eculturetool.ui.user;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.user.User;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;

public class ForgetPasswordViewModel extends AbstractUserViewModel {

    public ForgetPasswordViewModel(Application application) {
        super(application);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MutableLiveData<RepositoryNotification<Void>> changePassword() {
        User user = new User(email);
        return repository.changePassword(user);
    }

}
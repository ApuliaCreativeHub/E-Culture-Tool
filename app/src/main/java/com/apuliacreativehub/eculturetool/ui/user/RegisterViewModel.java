package com.apuliacreativehub.eculturetool.ui.user;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.user.User;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;

public class RegisterViewModel extends AbstractUserViewModel {

    public RegisterViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<Void>> registerUser() {
        User user = new User(name, surname, email, password, isCurator);
        return repository.registerUser(user);
    }

}
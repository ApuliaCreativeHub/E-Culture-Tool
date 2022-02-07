package com.apuliacreativehub.eculturetool.ui.user.viewmodel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.user.User;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;

public class EditProfileViewModel extends AbstractUserViewModel{

    public EditProfileViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<User>> editDetails() {
        User user = new User(name, surname, email, password, isCurator);
        return repository.editUser(user);
    }

    public MutableLiveData<RepositoryNotification<Void>> deleteUser(){
        User user = new User(email);
        return repository.deleteUser(user);
    }

}
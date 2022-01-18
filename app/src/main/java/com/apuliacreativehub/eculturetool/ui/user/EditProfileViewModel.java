package com.apuliacreativehub.eculturetool.ui.user;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.User;
import com.apuliacreativehub.eculturetool.data.entity.UserWithToken;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;

public class EditProfileViewModel extends AbstractUserViewModel{

    public EditProfileViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<RepositoryNotification<User>> editDetails() {
        User user = new User(name, surname, email, password, isCurator);
        return repository.editUser(user);
    }
}

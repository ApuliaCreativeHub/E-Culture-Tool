package com.apuliacreativehub.eculturetool.ui.user.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.apuliacreativehub.eculturetool.data.repository.UserRepository;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

public class LogoutViewModel extends AndroidViewModel {
    private final UserRepository repository;

    public LogoutViewModel(Application application) {
        super(application);
        ECultureTool app = getApplication();
        this.repository = new UserRepository(app.executorService);
    }

    public void logoutUser() {
        repository.logoutUser();
    }

}
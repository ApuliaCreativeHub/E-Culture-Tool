package com.apuliacreativehub.eculturetool.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.apuliacreativehub.eculturetool.data.repository.RepositoryCallback;

import java.lang.reflect.InvocationTargetException;

public class RegisterViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final RepositoryCallback repositoryCallback;

    public RegisterViewModelFactory(Application application, RepositoryCallback repositoryCallback) {
        this.application = application;
        this.repositoryCallback = repositoryCallback;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(Application.class, RepositoryCallback.class).newInstance(application, repositoryCallback);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }
}

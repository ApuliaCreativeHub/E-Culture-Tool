package com.apuliacreativehub.eculturetool.ui.user.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.user.Token;
import com.apuliacreativehub.eculturetool.data.entity.user.User;
import com.apuliacreativehub.eculturetool.data.entity.user.UserWithToken;
import com.apuliacreativehub.eculturetool.data.repository.PathRepository;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.data.repository.UserRepository;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

public class LoginViewModel extends AndroidViewModel {
    private String email = "";
    private String password = "";
    private final UserRepository userRepository;
    private final PathRepository pathRepository;
    private String uuid;

    public LoginViewModel(Application application) {
        super(application);
        ECultureTool app = getApplication();
        this.userRepository = new UserRepository(app.executorService);
        this.pathRepository = new PathRepository(app.executorService, app.localDatabase, (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public MutableLiveData<RepositoryNotification<UserWithToken>> loginUser() {
        User user = new User(email, password);
        Token token = new Token("", null, uuid);
        UserWithToken uwt = new UserWithToken(user, token);   // contains user with his UUID
        return userRepository.loginUser(uwt);
    }

    public MutableLiveData<Integer> checkForVisitorPaths() {
        return pathRepository.countVisitorPaths();
    }

    public void importVisitorPathsToAccount() {
        pathRepository.saveVisitorPathsToRemoteDatabase();
    }
}
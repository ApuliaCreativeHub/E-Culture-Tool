package com.apuliacreativehub.eculturetool.data.repository;

import com.apuliacreativehub.eculturetool.data.entity.User;
import com.apuliacreativehub.eculturetool.data.network.RemoteUserDAO;
import com.apuliacreativehub.eculturetool.data.network.UserRemoteDatabase;

public class UserRepository {
    RemoteUserDAO remoteUserDAO;

    public UserRepository() {
        remoteUserDAO = UserRemoteDatabase.provideRemoteUserDAO();
    }

    public void registerUser(User user) {
        remoteUserDAO.RegisterUser(user);
    }
}

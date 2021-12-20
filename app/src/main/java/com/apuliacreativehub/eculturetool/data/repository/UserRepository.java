package com.apuliacreativehub.eculturetool.data.repository;

import com.apuliacreativehub.eculturetool.data.entity.User;
import com.apuliacreativehub.eculturetool.data.network.RemoteUserDAO;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

//TODO: resolve entryPoint's problem
@AndroidEntryPoint
public class UserRepository {
    @Inject
    RemoteUserDAO remoteUserDAO;

    public void registerUser(User user){
        remoteUserDAO.RegisterUser(user);
    }
}

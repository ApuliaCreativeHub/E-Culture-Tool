package com.apuliacreativehub.eculturetool.data.repository;

import com.apuliacreativehub.eculturetool.data.entity.User;
import com.apuliacreativehub.eculturetool.data.network.RemoteUserDAO;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

import javax.inject.Inject;

public class UserRepository{
    @Inject
    RemoteUserDAO remoteUserDAO; //TODO: resolve nullPointerException

    public void registerUser(User user){
        remoteUserDAO.RegisterUser(user);
    }
}

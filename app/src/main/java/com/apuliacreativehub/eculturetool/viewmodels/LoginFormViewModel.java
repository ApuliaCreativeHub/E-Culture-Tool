package com.apuliacreativehub.eculturetool.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginFormViewModel extends ViewModel {
    private String email="";

    private String password="";

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

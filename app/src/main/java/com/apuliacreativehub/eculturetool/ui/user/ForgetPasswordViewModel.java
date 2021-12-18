package com.apuliacreativehub.eculturetool.ui.user;

import androidx.lifecycle.ViewModel;

public class ForgetPasswordViewModel extends ViewModel {

    private String email = "";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
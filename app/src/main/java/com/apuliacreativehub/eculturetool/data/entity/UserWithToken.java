package com.apuliacreativehub.eculturetool.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserWithToken {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("token")
    @Expose
    private Token token;

    public UserWithToken(User user, Token token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

}
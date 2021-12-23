package com.apuliacreativehub.eculturetool.data.entity;

import java.util.Date;

public class Token {
    private String token;
    private Date createdAt;
    private String uuid;

    public Token(String token, Date createdAt, String uuid) {
        this.token = token;
        this.createdAt = createdAt;
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

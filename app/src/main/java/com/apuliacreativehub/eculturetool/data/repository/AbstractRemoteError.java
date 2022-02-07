package com.apuliacreativehub.eculturetool.data.repository;

abstract public class AbstractRemoteError {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

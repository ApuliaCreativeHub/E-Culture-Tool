package com.apuliacreativehub.eculturetool.data.repository;

public class RemoteError extends AbstractRemoteError {
    private String statusCode;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}
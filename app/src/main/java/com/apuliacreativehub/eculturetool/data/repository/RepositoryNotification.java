package com.apuliacreativehub.eculturetool.data.repository;

public class RepositoryNotification<T> implements RepositoryNotificationInterface<T> {
    private T data;
    private Exception exception;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}

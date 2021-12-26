package com.apuliacreativehub.eculturetool.data.repository;

public class RepositoryNotification<T> implements RepositoryNotificationInterface<T> {
    private T data;
    private String errorMessage;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

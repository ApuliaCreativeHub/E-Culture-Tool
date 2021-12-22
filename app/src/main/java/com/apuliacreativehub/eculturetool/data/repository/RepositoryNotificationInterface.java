package com.apuliacreativehub.eculturetool.data.repository;

public interface RepositoryNotificationInterface<T> {
    T getData();

    void setData(T data);

    Exception getException();

    void setException(Exception exception);
}

package com.apuliacreativehub.eculturetool.data.repository;

import java.io.IOException;

import retrofit2.Response;

public interface RepositoryCallback<T> {
    void onComplete(Response<T> response);

    void onException(IOException ioe);
}

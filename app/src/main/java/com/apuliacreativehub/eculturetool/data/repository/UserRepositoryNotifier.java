package com.apuliacreativehub.eculturetool.data.repository;

import android.os.Handler;

import java.io.IOException;

import retrofit2.Response;

public class UserRepositoryNotifier implements Notifier{
    private final Handler resultHandler;

    public UserRepositoryNotifier(Handler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public <T> void notifyResult(final Response<T> response, final RepositoryCallback<T> callback) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(response);
            }
        });
    }

    public <T> void notifyError(final IOException ioe, RepositoryCallback<T> callback) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onException(ioe);
            }
        });
    }
}

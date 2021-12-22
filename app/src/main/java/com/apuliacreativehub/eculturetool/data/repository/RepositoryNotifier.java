package com.apuliacreativehub.eculturetool.data.repository;

import android.os.Handler;

import java.io.IOException;

import retrofit2.Response;

public class RepositoryNotifier implements Notifier{
    private final Handler resultHandler;
    private final RepositoryCallback callback;

    public RepositoryNotifier(Handler resultHandler, RepositoryCallback callback) {
        this.resultHandler = resultHandler;
        this.callback = callback;
    }

    public <T> void notifyResult(final Response<T> response) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(response);
            }
        });
    }

    public <T> void notifyError(final IOException ioe) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onException(ioe);
            }
        });
    }
}

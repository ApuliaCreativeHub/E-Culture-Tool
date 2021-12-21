package com.apuliacreativehub.eculturetool.data.repository;

import android.os.Handler;
import android.util.Log;

import com.apuliacreativehub.eculturetool.data.entity.User;
import com.apuliacreativehub.eculturetool.data.network.RemoteUserDAO;
import com.apuliacreativehub.eculturetool.data.network.UserRemoteDatabase;

import java.io.IOException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Response;

public class UserRepository {
    RemoteUserDAO remoteUserDAO;
    private final Executor executor;
    private final Handler resultHandler;

    public UserRepository(Executor executor, Handler resultHandler) {
        remoteUserDAO = UserRemoteDatabase.provideRemoteUserDAO();
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public void registerUser(User user) {
        Call<Void> call = remoteUserDAO.RegisterUser(user);

        // TODO: Pass callbacks implementation in constructor
        RepositoryCallback<Void> callback = new RepositoryCallback<Void>() {
            @Override
            public void onComplete(Response<Void> response) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", String.valueOf(response.code()));
            }

            @Override
            public void onException(IOException ioe) {
                Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
                Log.d("CALLBACK", "An exception occurred: " + ioe.getMessage());
            }
        };

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Void> response = call.execute();
                    Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                    notifyResult(response, callback);
                } catch (IOException ioe) {
                    notifyError(ioe, callback);
                    Log.e("RETROFITERROR", ioe.getMessage());
                }
            }
        });
    }

    private void notifyResult(final Response response, final RepositoryCallback callback) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(response);
            }
        });
    }

    private void notifyError(final IOException ioe, RepositoryCallback callback) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onException(ioe);
            }
        });
    }
}

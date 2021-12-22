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

public class UserRepository{
    private final RemoteUserDAO remoteUserDAO;
    private final Executor executor;
    private Notifier notifier;

    public void setNotifier(RepositoryNotifier notifier) {
        this.notifier = notifier;
    }

    public UserRepository(Executor executor, RepositoryNotifier notifier) {
        remoteUserDAO = UserRemoteDatabase.provideRemoteUserDAO();
        this.executor = executor;
        this.notifier = notifier;
    }

    public void registerUser(User user) {
        Call<Void> call = remoteUserDAO.RegisterUser(user);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Void> response = call.execute();
                    Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                    notifier.notifyResult(response);
                } catch (IOException ioe) {
                    Log.e("RETROFITERROR", ioe.getMessage());
                    notifier.notifyError(ioe);
                }
            }
        });
    }
}

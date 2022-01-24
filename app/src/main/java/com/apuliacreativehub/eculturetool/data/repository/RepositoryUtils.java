package com.apuliacreativehub.eculturetool.data.repository;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RepositoryUtils {
    public static final int FROM_LOCAL_DATABASE = 0;
    public static final int FROM_REMOTE_DATABASE = 1;

    public static int shouldFetch(ConnectivityManager connectivityManager) {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return FROM_REMOTE_DATABASE;
        else return FROM_LOCAL_DATABASE;
    }
}

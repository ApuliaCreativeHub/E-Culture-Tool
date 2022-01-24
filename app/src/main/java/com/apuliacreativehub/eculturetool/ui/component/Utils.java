package com.apuliacreativehub.eculturetool.ui.component;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
    public static boolean checkConnection(ConnectivityManager connectivityManager) {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

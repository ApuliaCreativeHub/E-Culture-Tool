package com.apuliacreativehub.eculturetool.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.apuliacreativehub.eculturetool.R;

public class TokenManager {
    private static String token = "";

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        TokenManager.token = token;
    }

    public static void setTokenFromSharedPreferences(Context context) {
        if (context != null) {
            SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
            token = sharedPref.getString("token", "");
        }
    }

}
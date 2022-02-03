package com.apuliacreativehub.eculturetool.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.entity.user.User;

public class UserPreferencesManager {
    private static String token = "";
    private static User user;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserPreferencesManager.token = token;
    }

    public static User getUser() {
        return user;
    }

    public static void setUserInfo(String name, String surname, String email, boolean isACurator) {
        user = new User(name, surname, email, "", isACurator);
    }

    public static void setUserInfoFromSharedPreferences(Context context) {
        if (context != null) {
            SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.login_shared_preferences), Context.MODE_PRIVATE);
            token = sharedPref.getString("token", "");
            user = new User(sharedPref.getString("name", ""),
                    sharedPref.getString("surname", ""),
                    sharedPref.getString("email", ""),
                    "",
                    sharedPref.getBoolean("isACurator", false));
        }
    }
}

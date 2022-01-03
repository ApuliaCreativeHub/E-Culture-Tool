package com.apuliacreativehub.eculturetool.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class UuidManager {
    private static String uuid = null;

    public synchronized static String getUuid(Context context, String sharedPrefsName) {
        if (uuid == null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
            uuid = sharedPreferences.getString("uuid", null);
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("uuid", uuid);
                editor.apply();
            }
        }
        return uuid;
    }
}

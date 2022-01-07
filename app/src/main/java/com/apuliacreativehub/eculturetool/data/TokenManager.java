package com.apuliacreativehub.eculturetool.data;

public class TokenManager {
    private static String token = "";

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        TokenManager.token = token;
    }
}

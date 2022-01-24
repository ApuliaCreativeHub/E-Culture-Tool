package com.apuliacreativehub.eculturetool.data;

import android.content.res.Resources;

import com.apuliacreativehub.eculturetool.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ErrorStrings {
    private static ErrorStrings instance = null;
    public Map<String, String> errors = null;

    public static ErrorStrings getInstance(Resources resources) {
        if (instance == null) {
            instance = new ErrorStrings();
            instance.errors = new HashMap<>();
            instance.errors.put("40", resources.getString(R.string.e40));
            instance.errors.put("41", resources.getString(R.string.e41));
            instance.errors.put("42", resources.getString(R.string.e42));
            instance.errors.put("43", resources.getString(R.string.e43));
            instance.errors.put("44", resources.getString(R.string.e44));
            instance.errors.put("45", resources.getString(R.string.e45));

            instance.errors.put("50", resources.getString(R.string.e50));
            instance.errors.put("51", resources.getString(R.string.e51));
            instance.errors.put("52", resources.getString(R.string.e52));
            instance.errors.put("53", resources.getString(R.string.e53));
            instance.errors.put("54", resources.getString(R.string.e54));
            instance.errors.put("55", resources.getString(R.string.e55));
            instance.errors = Collections.unmodifiableMap(instance.errors);
        }
        return instance;
    }

}
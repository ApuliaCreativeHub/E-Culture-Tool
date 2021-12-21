package com.apuliacreativehub.eculturetool.di;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ECultureTool extends Application {
    public final ExecutorService executorService = Executors.newFixedThreadPool(4);
    public final Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
}

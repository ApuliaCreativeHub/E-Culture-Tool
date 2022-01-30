package com.apuliacreativehub.eculturetool.di;

import android.app.Application;

import com.apuliacreativehub.eculturetool.data.local.LocalDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ECultureTool extends Application {

    public final ExecutorService executorService = Executors.newFixedThreadPool(4);
    public LocalDatabase localDatabase;

}
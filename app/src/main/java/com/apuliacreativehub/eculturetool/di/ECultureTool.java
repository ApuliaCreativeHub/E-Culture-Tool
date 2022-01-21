package com.apuliacreativehub.eculturetool.di;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ECultureTool extends Application {

    public final ExecutorService executorService = Executors.newFixedThreadPool(4);

}
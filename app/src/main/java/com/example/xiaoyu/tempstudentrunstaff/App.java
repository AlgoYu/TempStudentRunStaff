package com.example.xiaoyu.tempstudentrunstaff;

import android.app.Application;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class App extends Application{
    public static App app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}

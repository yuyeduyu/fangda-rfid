package com.example.uhfsdkdemo;

import android.app.Application;

import com.example.uhfsdkdemo.utils.CrashHandler;

/**
 * 作者：lish on 2019-03-11 9:24
 * 描述：
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}

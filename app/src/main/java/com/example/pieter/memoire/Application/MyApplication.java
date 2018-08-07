package com.example.pieter.memoire.Application;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class MyApplication extends Application {

    RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context)
    {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
       // refWatcher = LeakCanary.install(this);
    }
}

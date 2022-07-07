package com.example.netnew;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
    public static MyApplication Instance;
    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
    }
    private Activity mMainActivity;

    public Activity getMainActivity() {
        return mMainActivity;
    }

    public  void setMainActivity(Activity mainActivity) {
        mMainActivity = mainActivity;
    }
}

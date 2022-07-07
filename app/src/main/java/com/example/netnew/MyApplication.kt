package com.example.netnew

import android.app.Application
import com.example.netnew.MyApplication
import android.app.Activity

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Instance = this
    }

    var mainActivity: Activity? = null

    companion object {
        var Instance: MyApplication? = null
    }
}
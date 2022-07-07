package com.example.netnew.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //说明当前有网络
            if (networkInfo != null && networkInfo.isAvailable()) {
                int type = networkInfo.getType();
                switch (type) {
                    case ConnectivityManager.TYPE_MOBILE:
                        Toast.makeText(context, "当前移动网络正常", Toast.LENGTH_SHORT).show();
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        Toast.makeText(context, "当前WIFI网络正常", Toast.LENGTH_SHORT).show();
                        break;
                    case ConnectivityManager.TYPE_ETHERNET:
                        Toast.makeText(context, "当前以太网网络正常", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else {
                //说明当前没有网络
                Toast.makeText(context, "当前网络异常", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
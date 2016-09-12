package com.kuainiutv.command.sevice;

import android.content.Intent;
import android.util.Log;

import com.easefun.polyvsdk.server.AndroidService;

/**
 * @author nanck on 2016/4/8.
 */
public class PolyvDemoService extends AndroidService {
    private static final String TAG = "PolyvDemoService";

    // 无参数构造函数，调用父类的super(String name)
    public PolyvDemoService() {
        super();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "server destory");
    }
}

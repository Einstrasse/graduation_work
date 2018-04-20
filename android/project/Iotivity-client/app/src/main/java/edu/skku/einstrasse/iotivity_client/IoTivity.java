package edu.skku.einstrasse.iotivity_client;

import android.app.Application;
import android.content.Context;

/**
 * Created by hg958 on 2018-04-21.
 */

public class IoTivity extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        IoTivity.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return IoTivity.context;
    }
}

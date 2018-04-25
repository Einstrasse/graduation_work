package edu.skku.einstrasse.iotivity_client;

import android.app.Application;
import android.content.Context;

import org.iotivity.base.OcResource;

import edu.skku.einstrasse.iotivity_client.oic.res.WeeklyAlarmHandler;

/**
 * Created by hg958 on 2018-04-21.
 */

public class IoTivity extends Application {
    private static Context context;
    private static WeeklyAlarmHandler weeklyAlarmHandler = null;
    private static OcResource weeklyAlarmHandlerResource = null;

    @Override
    public void onCreate() {
        super.onCreate();
        IoTivity.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return IoTivity.context;
    }
    public static WeeklyAlarmHandler getWeeklyAlarmHandler() { return weeklyAlarmHandler; }
    public static void setWeeklyAlarmHandler(WeeklyAlarmHandler obj) { weeklyAlarmHandler = obj; }
    public static OcResource getWeeklyAlarmHandlerResource() { return weeklyAlarmHandlerResource; }
    public static void setWeeklyAlarmHandlerResource(OcResource obj) { weeklyAlarmHandlerResource = obj; }
}

package edu.skku.einstrasse.iotivity_client.oic.res;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hg958 on 2018-04-20.
 */

public class AlarmJSONData {
    ArrayList<WeeklyAlarm> weekly;

    public AlarmJSONData() {
        weekly = new ArrayList<WeeklyAlarm>();
    }
    public class WeeklyAlarm {
        int id;
        String name;
        int hour;
        int min;
        int day;
        boolean enabled;
        private String leftPad(int val) {
            if (val < 10) {
                return "0" + Integer.toString(val);
            }
            return Integer.toString(val);
        }
        public String getTimeString() {
            return leftPad(hour) + ":" + leftPad(min);
        }
        public int getDay() {
            return day;
        }
        void dump() {
            Log.e("Einstrasse@@@", "id:" + id);
            Log.e("Einstrasse@@@", "name:" + name);
            Log.e("Einstrasse@@@", "hour:" + hour);
            Log.e("Einstrasse@@@", "min:" + min);
            Log.e("Einstrasse@@@", "day:" + day);
            Log.e("Einstrasse@@@", "enabled:" + enabled);
        }
    }
}

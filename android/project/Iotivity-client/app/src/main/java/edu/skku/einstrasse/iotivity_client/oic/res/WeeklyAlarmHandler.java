package edu.skku.einstrasse.iotivity_client.oic.res;

import android.util.Log;

import com.google.gson.Gson;

import org.iotivity.base.OcException;
import org.iotivity.base.OcRepresentation;

import java.util.ArrayList;

/**
 * Created by hg958 on 2018-04-13.
 */

public class WeeklyAlarmHandler {
    public static final String NAME_KEY = "name";
    public static final String ALARMCOUNT_KEY = "alarmCount";
    public static final String SERIALIZEDDATA_KEY = "serializedData";

    private String mName;
    private int mAlarmCount;
    private String mSerializedData;
    private AlarmJSONData jsonData = null;
//    private ArrayList<WeeklyAlarm> mWeeklyAlarms = null;
    // TODO : WeeklyAlarm 클래스 만들어서, 객체로서 핸들러에서 가지고있도록 변경할 것!

    public WeeklyAlarmHandler() {
        mName = "";
        mAlarmCount = 0;
        mSerializedData = "{}";
        jsonData = new AlarmJSONData();
    }

    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        mName = rep.getValue(NAME_KEY);
        mAlarmCount = rep.getValue(ALARMCOUNT_KEY);
        // TODO : sql구문 실패하였을 때, 해당 값에 boolean 값이 들어가는 이슈 해결 필요
        mSerializedData = rep.getValue(SERIALIZEDDATA_KEY);

        Gson gson = new Gson();
        jsonData = gson.fromJson(mSerializedData, AlarmJSONData.class);

        //debug!
        for (AlarmJSONData.WeeklyAlarm it : jsonData.weekly) {
            it.dump();
        }
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        rep.setValue(NAME_KEY, mName);
        rep.setValue(ALARMCOUNT_KEY, mAlarmCount);
        if (null != jsonData) {
            Gson gson = new Gson();
            mSerializedData = gson.toJson(jsonData);
        }
        rep.setValue(SERIALIZEDDATA_KEY, mSerializedData);
        return rep;
    }

    public ArrayList<AlarmJSONData.WeeklyAlarm> getWeeklyAlarmList() {
        if (null == jsonData || null == jsonData.weekly) {
            return null;
        }
        return jsonData.weekly;
    }
    public String getName() { return mName; }
    public void setName(String name) { this.mName = name; }
    public int getAlarmCount() { return mAlarmCount; }
    public String getSerializedData(){ return mSerializedData; }
}

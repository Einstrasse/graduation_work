package edu.skku.einstrasse.iotivity_client.oic.res;

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

    public class WeeklyAlarm {
        int mId;
        String mName;
        int mHour;
        int mMin;
        int mDay;
        boolean mEnabled;
    }


    private String mName;
    private int mAlarmCount;
    private String mSerializedData;
    private ArrayList<WeeklyAlarm> mWeeklyAlarms = new ArrayList<WeeklyAlarm>();
    // TODO : WeeklyAlarm 클래스 만들어서, 객체로서 핸들러에서 가지고있도록 변경할 것!

    public WeeklyAlarmHandler() {
        mName = "";
        mAlarmCount = 0;
        mSerializedData = "{}";
    }

    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        mName = rep.getValue(NAME_KEY);
        mAlarmCount = rep.getValue(ALARMCOUNT_KEY);
        mSerializedData = rep.getValue(SERIALIZEDDATA_KEY);
        // TODO : WeeklyAlarm 클래스를 만들어서 객체로서 가지고 있도록 할 것
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        rep.setValue(NAME_KEY, mName);
        rep.setValue(ALARMCOUNT_KEY, mAlarmCount);
        rep.setValue(SERIALIZEDDATA_KEY, mSerializedData);
        return rep;
    }

    public ArrayList<WeeklyAlarm> getWeeklyAlarms() { return mWeeklyAlarms; }
    public String getName() { return mName; }
    public void setName(String name) { this.mName = name; }
    public int getAlarmCount() { return mAlarmCount; }
    public String getSerializedData(){ return mSerializedData; }
}

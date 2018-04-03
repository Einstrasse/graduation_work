package edu.skku.einstrasse.iotivity_client;

/**
 * Created by hg958 on 2018-04-03.
 */

import org.iotivity.base.OcException;
import org.iotivity.base.OcRepresentation;

/**
 * Light
 * <p/>
 * This class is used by SimpleClient to create an object representation of a remote light resource
 * and update the values depending on the server response
 */
public class Light {
    public static final String NAME_KEY =  "name";
    public static final String SWITCH_KEY = "switch";
    public static final String BRIGHTNESS_KEY = "brightness";
    public static final String DEFAULTANGLE_KEY = "defaultAngle";
    public static final String ONANGLE_KEY = "onAngle";
    public static final String OFFANGLE_KEY = "offAngle";
//    public static final String STATE_KEY = "state";
//    public static final String POWER_KEY = "power";

    private String mName;
    private boolean mSwitch;
    private int mBrightness;
    private int mDefaultAngle;
    private int mOnAngle;
    private int mOffAngle;

    public Light() {
        mName = "";
        mSwitch = false;
        mBrightness = 0;
        mDefaultAngle = 90;
        mOnAngle = 105;
        mOffAngle = 75;
    }

    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        mName = rep.getValue(NAME_KEY);
        mSwitch = rep.getValue(Light.SWITCH_KEY);
        mBrightness = rep.getValue(Light.BRIGHTNESS_KEY);
        mDefaultAngle = rep.getValue(Light.DEFAULTANGLE_KEY);
        mOnAngle = rep.getValue(Light.ONANGLE_KEY);
        mOffAngle = rep.getValue(Light.OFFANGLE_KEY);
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        rep.setValue(NAME_KEY, mName);
        rep.setValue(SWITCH_KEY, mSwitch);
        rep.setValue(BRIGHTNESS_KEY, mBrightness);
        rep.setValue(DEFAULTANGLE_KEY, mDefaultAngle);
        rep.setValue(ONANGLE_KEY, mOnAngle);
        rep.setValue(OFFANGLE_KEY, mOffAngle);

        return rep;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public boolean getSwitch() {
        return mSwitch;
    }

    public void setState(boolean swt) {
        this.mSwitch = swt;
    }

    public int getBrightness() {
        return mBrightness;
    }

    public void setBrightness(int brightness) {
        this.mBrightness = brightness;
    }

    public int getmDefaultAngle() {
        return mDefaultAngle;
    }

    public void setDefaultAngle(int angle) {
        this.mDefaultAngle = angle;
    }

    public int getOnAngle() {
        return mOnAngle;
    }

    public void setOnAngle(int angle) {
        this.mOnAngle = angle;
    }

    public int getOffAngle() {
        return mOffAngle;
    }

    public void setOffAngle(int angle) {
        this.mOffAngle = angle;
    }

    @Override
    public String toString() {
        return "\t" + NAME_KEY + ": " + mName +
                "\n\t" + SWITCH_KEY + ": " + mSwitch +
                "\n\t" + BRIGHTNESS_KEY + ": " + mBrightness +
                "\n\t" + DEFAULTANGLE_KEY + ": " + mDefaultAngle +
                "\n\t" + ONANGLE_KEY + ": " + mOnAngle +
                "\n\t" + OFFANGLE_KEY + ": " + mOffAngle;
    }
}

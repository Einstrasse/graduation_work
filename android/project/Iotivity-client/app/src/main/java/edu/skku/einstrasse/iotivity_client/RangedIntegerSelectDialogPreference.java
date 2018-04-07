package edu.skku.einstrasse.iotivity_client;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by hg958 on 2018-04-04.
 */

public class RangedIntegerSelectDialogPreference extends DialogPreference {

    private int value;
    private int DEFAULT_VALUE = 90;
    private NumberPicker numberPicker = null;
    public RangedIntegerSelectDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // onCreateDialogView에서 LayoutInflater로 View를 생성하므로 불필요함.
        //setDialogLayoutResource(R.layout.number_picker_dialog);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            value = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            value = (Integer) defaultValue;
            persistInt(value);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected View onCreateDialogView() {
        int min = 40;
        int max = 140;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.number_picker_dialog, null);

        numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(this.getPersistedInt(DEFAULT_VALUE));
        numberPicker.setWrapSelectorWheel(true);

        return view;
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {

        if (positiveResult) {
            persistInt(numberPicker.getValue());
        }
        super.onDialogClosed(positiveResult);
    }
}

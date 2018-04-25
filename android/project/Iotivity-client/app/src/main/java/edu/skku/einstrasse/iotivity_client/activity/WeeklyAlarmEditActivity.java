package edu.skku.einstrasse.iotivity_client.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.skku.einstrasse.iotivity_client.IoTivity;
import edu.skku.einstrasse.iotivity_client.R;
import edu.skku.einstrasse.iotivity_client.oic.res.AlarmJSONData;

public class WeeklyAlarmEditActivity extends AppCompatActivity {

    Button button_cancel;
    Button button_edit;
    Button button_reset;
    TextView alarm_name_text;
    EditText alarm_hour_text;
    EditText alarm_min_text;
    CheckBox alarm_day_checkbox_1;
    CheckBox alarm_day_checkbox_2;
    CheckBox alarm_day_checkbox_3;
    CheckBox alarm_day_checkbox_4;
    CheckBox alarm_day_checkbox_5;
    CheckBox alarm_day_checkbox_6;
    CheckBox alarm_day_checkbox_7;

    String alarmName;
    int alarmHour;
    int alarmMin;

    int alarmDay;
    int alarmId;

    void setFormData() {
        alarm_name_text.setText(alarmName);
        alarm_hour_text.setText(AlarmJSONData.leftPad(alarmHour));
        alarm_min_text.setText(AlarmJSONData.leftPad(alarmMin));

        if ( (alarmDay & (1 << ( 1 - 1 ) ) ) != 0) {
            alarm_day_checkbox_1.setChecked(true);
        } else {
            alarm_day_checkbox_1.setChecked(false);
        }
        if ( (alarmDay & (1 << ( 2 - 1 ) ) ) != 0) {
            alarm_day_checkbox_2.setChecked(true);
        } else {
            alarm_day_checkbox_2.setChecked(false);
        }
        if ( (alarmDay & (1 << ( 3 - 1 ) ) ) != 0) {
            alarm_day_checkbox_3.setChecked(true);
        } else {
            alarm_day_checkbox_3.setChecked(false);
        }
        if ( (alarmDay & (1 << ( 4 - 1 ) ) ) != 0) {
            alarm_day_checkbox_4.setChecked(true);
        } else {
            alarm_day_checkbox_4.setChecked(false);
        }
        if ( (alarmDay & (1 << ( 5 - 1 ) ) ) != 0) {
            alarm_day_checkbox_5.setChecked(true);
        } else {
            alarm_day_checkbox_5.setChecked(false);
        }
        if ( (alarmDay & (1 << ( 6 - 1 ) ) ) != 0) {
            alarm_day_checkbox_6.setChecked(true);
        } else {
            alarm_day_checkbox_6.setChecked(false);
        }
        if ( (alarmDay & (1 << ( 7 - 1 ) ) ) != 0) {
            alarm_day_checkbox_7.setChecked(true);
        } else {
            alarm_day_checkbox_7.setChecked(false);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_alarm_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        alarmName = intent.getStringExtra("alarmName");
        alarmHour = intent.getIntExtra("hour", 0);
        alarmMin = intent.getIntExtra("min", 0);
        alarmDay = intent.getIntExtra("day", 0);
        alarmId = intent.getIntExtra("id", -1);

        alarm_name_text = (TextView) findViewById(R.id.alarm_name_text);

        alarm_hour_text = (EditText) findViewById(R.id.alarm_hour_text);

        alarm_min_text = (EditText) findViewById(R.id.alarm_min_text);


        alarm_day_checkbox_1 = (CheckBox) findViewById(R.id.alarm_day_checkbox_1);
        alarm_day_checkbox_2 = (CheckBox) findViewById(R.id.alarm_day_checkbox_2);
        alarm_day_checkbox_3 = (CheckBox) findViewById(R.id.alarm_day_checkbox_3);
        alarm_day_checkbox_4 = (CheckBox) findViewById(R.id.alarm_day_checkbox_4);
        alarm_day_checkbox_5 = (CheckBox) findViewById(R.id.alarm_day_checkbox_5);
        alarm_day_checkbox_6 = (CheckBox) findViewById(R.id.alarm_day_checkbox_6);
        alarm_day_checkbox_7 = (CheckBox) findViewById(R.id.alarm_day_checkbox_7);

        setFormData();

        button_cancel = (Button) findViewById(R.id.button_cancel);
        button_edit = (Button) findViewById(R.id.button_edit);
        button_reset = (Button) findViewById(R.id.button_reset);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ret = new Intent();
                setResult(RESULT_CANCELED, ret);
                finish();
            }
        });

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ret = new Intent();
                String hour_string = alarm_hour_text.getText().toString();
                String min_string = alarm_min_text.getText().toString();

                if (hour_string.length() <= 0 || min_string.length() <= 0) {
                    Toast.makeText(IoTivity.getAppContext(), getString(R.string.invalid_time), Toast.LENGTH_SHORT).show();
                    return;
                }
                int hour = Integer.parseInt(hour_string);
                int min = Integer.parseInt(min_string);
                if (hour >= 24 || hour < 0) {
                    Toast.makeText(IoTivity.getAppContext(), getString(R.string.invalid_time), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (min < 0 || min >= 60) {
                    Toast.makeText(IoTivity.getAppContext(), getString(R.string.invalid_time), Toast.LENGTH_SHORT).show();
                    return;
                }
                String alarm_name = alarm_name_text.getText().toString();
                if (alarm_name.length() <= 0) {
                    alarm_name = getString(R.string.alarm);
                }
                ret.putExtra("name", alarm_name);
                ret.putExtra("hour", hour);
                ret.putExtra("min", min);
                int day = 0;
                if (alarm_day_checkbox_1.isChecked()) day |= (1 << (1 - 1));
                if (alarm_day_checkbox_2.isChecked()) day |= (1 << (2 - 1));
                if (alarm_day_checkbox_3.isChecked()) day |= (1 << (3 - 1));
                if (alarm_day_checkbox_4.isChecked()) day |= (1 << (4 - 1));
                if (alarm_day_checkbox_5.isChecked()) day |= (1 << (5 - 1));
                if (alarm_day_checkbox_6.isChecked()) day |= (1 << (6 - 1));
                if (alarm_day_checkbox_7.isChecked()) day |= (1 << (7 - 1));
                ret.putExtra("day", day);
                ret.putExtra("id", alarmId);
                setResult(RESULT_OK, ret);
                finish();
            }
        });

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFormData();
            }
        });
    }
}

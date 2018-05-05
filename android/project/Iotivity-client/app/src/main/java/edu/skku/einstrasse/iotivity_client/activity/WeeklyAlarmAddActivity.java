package edu.skku.einstrasse.iotivity_client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.skku.einstrasse.iotivity_client.IoTivity;
import edu.skku.einstrasse.iotivity_client.R;

public class WeeklyAlarmAddActivity extends AppCompatActivity {

    private static final String TAG = "EINSTRASSE@@@@";
    private static void lg(final String msg) {
        Log.e(TAG, msg);
    }

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
    Button cancel_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_alarm_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

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

        cancel_button = (Button) findViewById(R.id.cancel_button);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
                if (null == IoTivity.getWeeklyAlarmHandlerResource()) {
                    Toast.makeText(IoTivity.getAppContext(), getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
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
                setResult(RESULT_OK, ret);
                finish();
            }
        });
    }

}

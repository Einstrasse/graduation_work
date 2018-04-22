package edu.skku.einstrasse.iotivity_client.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                finish();
            }
        });
    }

}

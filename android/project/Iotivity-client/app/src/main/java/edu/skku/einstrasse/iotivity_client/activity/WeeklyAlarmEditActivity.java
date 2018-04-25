package edu.skku.einstrasse.iotivity_client.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.skku.einstrasse.iotivity_client.R;

public class WeeklyAlarmEditActivity extends AppCompatActivity {

    Button button_cancel;
    Button button_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_alarm_edit);

        button_cancel = (Button) findViewById(R.id.button_cancel);
        button_edit = (Button) findViewById(R.id.button_edit);

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
                setResult(RESULT_OK, ret);
                finish();
            }
        });
    }
}

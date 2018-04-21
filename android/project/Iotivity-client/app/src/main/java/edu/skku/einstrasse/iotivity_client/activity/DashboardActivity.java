package edu.skku.einstrasse.iotivity_client.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.skku.einstrasse.iotivity_client.R;
import edu.skku.einstrasse.iotivity_client.fragment.AlarmFragment;
import edu.skku.einstrasse.iotivity_client.fragment.HomeFragment;

public class DashboardActivity extends AppCompatActivity {

    FragmentManager fragmentManager = null;
    FragmentTransaction fragmentTransaction = null;
    HomeFragment homeFragment = null;
    AlarmFragment alarmFragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.replace(R.id.dashboard_fragment_position, homeFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    //TODO: Navigation Dashboard에 어떤 기능을 넣을지 구상하고 넣을 것
                    return true;
                case R.id.navigation_alarm:
                    fragmentTransaction.replace(R.id.dashboard_fragment_position, alarmFragment);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.actionbar_setting:
                final Intent setting_intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(setting_intent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        fragmentManager = getFragmentManager();

        homeFragment = new HomeFragment();
        alarmFragment = new AlarmFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dashboard_fragment_position, homeFragment);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}

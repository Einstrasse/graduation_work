package edu.skku.einstrasse.iotivity_client.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import edu.skku.einstrasse.iotivity_client.R;
import edu.skku.einstrasse.iotivity_client.activity.AppCompatPreferenceActivity;
import edu.skku.einstrasse.iotivity_client.fragment.HomeFragment;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */

    // Constant
    private final static String TAG = HomeFragment.class.getSimpleName();
    private final static String ETAG = "Einstrasse@@@";

    private void showToast(final String msg, final int len){
        final Activity activity = this;
        if (null != activity) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Context context = activity.getApplicationContext();
                    if (null != context) {
                        Toast.makeText(context, msg, len).show();
                    } else {
                        lg("getApplicationContext() function returns null!!");
                    }
                }
            });
        }
    };

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            String new_val = sharedPreferences.getString(key, "Unknown");
//            lg("SharedPreference changes! " + key + " = " + new_val);
            showToast("Setting Value updated Successfully!", Toast.LENGTH_SHORT);

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pref.registerOnSharedPreferenceChangeListener(listener);
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pref.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                Log.e("Einstrasse@@@", "Undefined OptionsItem selected!!");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || LightPreferenceFragment.class.getName().equals(fragmentName);
    }


    /**
     * This fragment show general perferences only. It is used when the
     * activity is showing a two-pane settings Ui.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class LightPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            lg("LightPreferenceFragment onCreate event~!");
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_light);
            setHasOptionsMenu(false);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            return super.onOptionsItemSelected(item);
        }

    }

    private static void lg(final String txt) {
        Log.e(ETAG, txt);
    }

}

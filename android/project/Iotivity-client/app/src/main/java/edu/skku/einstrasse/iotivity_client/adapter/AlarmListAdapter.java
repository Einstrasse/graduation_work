package edu.skku.einstrasse.iotivity_client.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import edu.skku.einstrasse.iotivity_client.IoTivity;
import edu.skku.einstrasse.iotivity_client.R;
import edu.skku.einstrasse.iotivity_client.oic.res.AlarmJSONData;

/**
 * Created by hg958 on 2018-04-20.
 */

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {
    private List<AlarmJSONData.WeeklyAlarm> mWeeklyDataset = null;
    private static final String TAG = "Einstrasse@@ Adapter";

    private static void lg(final String msg) {
        Log.e(TAG, msg);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView alarm_time_text;
        public TextView short_day_1;
        public TextView short_day_2;
        public TextView short_day_3;
        public TextView short_day_4;
        public TextView short_day_5;
        public TextView short_day_6;
        public TextView short_day_7;
        public TextView alarm_name_text;
        public ToggleButton alarm_enabled_toggle;
        public ViewHolder(View v) {
            super(v);
            alarm_time_text = (TextView) v.findViewById(R.id.alarm_time_text);
            short_day_1 = (TextView) v.findViewById(R.id.short_day_1);
            short_day_2 = (TextView) v.findViewById(R.id.short_day_2);
            short_day_3 = (TextView) v.findViewById(R.id.short_day_3);
            short_day_4 = (TextView) v.findViewById(R.id.short_day_4);
            short_day_5 = (TextView) v.findViewById(R.id.short_day_5);
            short_day_6 = (TextView) v.findViewById(R.id.short_day_6);
            short_day_7 = (TextView) v.findViewById(R.id.short_day_7);
            alarm_name_text = (TextView) v.findViewById(R.id.alarm_name_text);
            alarm_enabled_toggle = (ToggleButton) v.findViewById(R.id.alarm_enabled_toggle);
        }

    }
    public AlarmListAdapter() {
        mWeeklyDataset = new ArrayList<AlarmJSONData.WeeklyAlarm>();
    }
    public AlarmListAdapter(List<AlarmJSONData.WeeklyAlarm> weeklyDataset) {
        mWeeklyDataset = weeklyDataset;
    }
    public void setData(List<AlarmJSONData.WeeklyAlarm> data) {
        this.mWeeklyDataset = data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        lg("onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_alarm_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        lg("onBindViewHolder");
        AlarmJSONData.WeeklyAlarm data = mWeeklyDataset.get(position);
        holder.alarm_time_text.setText(data.getTimeString());
        holder.alarm_name_text.setText(data.getName());
        holder.alarm_enabled_toggle.setChecked(data.getEnabled());
        int day = data.getDay();
        if ((day & (1 << (1-1))) == 0) {
            holder.short_day_1.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        }
        if ((day & (1 << (2-1))) == 0) {
            holder.short_day_2.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        }
        if ((day & (1 << (3-1))) == 0) {
            holder.short_day_3.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        }
        if ((day & (1 << (4-1))) == 0) {
            holder.short_day_4.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        }
        if ((day & (1 << (5-1))) == 0) {
            holder.short_day_5.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        }
        if ((day & (1 << (6-1))) == 0) {
            holder.short_day_6.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        }
        if ((day & (1 << (7-1))) == 0) {
            holder.short_day_7.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        }
    }

    @Override
    public int getItemCount() {
        lg("getItemCount()");
        if (null == mWeeklyDataset) return 0;
        return mWeeklyDataset.size();
    }
}

package edu.skku.einstrasse.iotivity_client.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.iotivity.base.OcException;
import org.iotivity.base.OcHeaderOption;
import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.skku.einstrasse.iotivity_client.IoTivity;
import edu.skku.einstrasse.iotivity_client.R;
import edu.skku.einstrasse.iotivity_client.activity.DashboardActivity;
import edu.skku.einstrasse.iotivity_client.activity.WeeklyAlarmEditActivity;
import edu.skku.einstrasse.iotivity_client.fragment.AlarmFragment;
import edu.skku.einstrasse.iotivity_client.oic.res.AlarmJSONData;

/**
 * Created by hg958 on 2018-04-20.
 */

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {
    private List<AlarmJSONData.WeeklyAlarm> mWeeklyDataset = null;
    private Activity mActivity = null;
    private Fragment mFragment = null;
    private OcResource.OnPutListener mOnPutListener = null;
    private static final String TAG = "Einstrasse@@ Adapter";

    private static void lg(final String msg) {
        Log.e(TAG, msg);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
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
            view = v;
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
    public AlarmListAdapter(OcResource.OnPutListener onPutListener, Activity activity, Fragment fragment) {
        mWeeklyDataset = new ArrayList<AlarmJSONData.WeeklyAlarm>();
        mOnPutListener = onPutListener;
        mActivity = activity;
        mFragment = fragment;
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
        final AlarmJSONData.WeeklyAlarm data = mWeeklyDataset.get(position);
        final int pos = position;
        final int id = data.getId();
        holder.alarm_time_text.setText(data.getTimeString());
        holder.alarm_name_text.setText(data.getName());

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context context = IoTivity.getAppContext();
                Resources res = context.getResources();
                Toast.makeText(context, "Long click!", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(mActivity)
                        .setTitle(res.getString(R.string.alarm_manipul_title))
                        .setMessage(res.getString(R.string.alarm_manipul_text))
                        .setPositiveButton(res.getString(R.string.edit), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO intent에 alarmEdit에 필요한 정보들을 넘겨주어야 한다.
                                // TODO 이후 alarmEdit Activity 구현하면됨.
                                Intent intent = new Intent(IoTivity.getAppContext(), WeeklyAlarmEditActivity.class);
                                mFragment.startActivityForResult(intent, AlarmFragment.REQUEST_CODE_ALARM_EDIT);

                            }
                        })
                        .setNegativeButton(res.getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO delete METHOD는 사용이 제한되어보이므로, POST에서 delete기능도 가능하게 서버단에서구현해야함... T.T
                            }
                        })
                        .setNeutralButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //아무 동작도 하지 않는다.
                            }
                        })
                        .show();
                return true;
            }
        });
        final int day = data.getDay();
        if ((day & (1 << (1-1))) == 0) {
            holder.short_day_1.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        } else {
            holder.short_day_1.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.black));
        }
        if ((day & (1 << (2-1))) == 0) {
            holder.short_day_2.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        } else {
            holder.short_day_2.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.black));
        }
        if ((day & (1 << (3-1))) == 0) {
            holder.short_day_3.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        } else {
            holder.short_day_3.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.black));
        }
        if ((day & (1 << (4-1))) == 0) {
            holder.short_day_4.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        } else {
            holder.short_day_4.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.black));
        }
        if ((day & (1 << (5-1))) == 0) {
            holder.short_day_5.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        }  else {
            holder.short_day_5.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.black));
        }
        if ((day & (1 << (6-1))) == 0) {
            holder.short_day_6.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        }  else {
            holder.short_day_6.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.black));
        }
        if ((day & (1 << (7-1))) == 0) {
            holder.short_day_7.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.gray));
        }  else {
            holder.short_day_7.setTextColor(ContextCompat.getColor(IoTivity.getAppContext(), R.color.black));
        }
        // setOnCheckedChangeListener에 반응하지 않도록 잠금
        holder.alarm_enabled_toggle.setEnabled(false);
        holder.alarm_enabled_toggle.setChecked(data.getEnabled());
        holder.alarm_enabled_toggle.setEnabled(true);
        holder.alarm_enabled_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isEnabled() == false) return;
                buttonView.setEnabled(false);
                Map<String, String> queryParams = new HashMap<>();
                queryParams.put("m_id", String.valueOf(id));
                queryParams.put("enabled", isChecked ? "1" : "0");
                OcRepresentation rep = null;
                lg("onCheckedChanged! id:" + String.valueOf(id) + "\nChecked: " + (isChecked ? "O" : "X"));
                try {
                    rep = IoTivity.getWeeklyAlarmHandler().getOcRepresentation();
                    IoTivity.getWeeklyAlarmHandlerResource().put(rep, queryParams, mOnPutListener);

                } catch (OcException e) {
                    lg("Error occured while enabled toggling!");
                    return;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        lg("getItemCount()");
        if (null == mWeeklyDataset) return 0;
        return mWeeklyDataset.size();
    }

}

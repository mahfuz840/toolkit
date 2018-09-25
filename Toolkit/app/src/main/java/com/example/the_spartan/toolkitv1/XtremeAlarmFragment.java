package com.example.the_spartan.toolkitv1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.the_spartan.toolkitv1.alarm.Alarm;
import com.example.the_spartan.toolkitv1.alarm.AlarmListAdapter;
import com.example.the_spartan.toolkitv1.alarm.database.Database;
import com.example.the_spartan.toolkitv1.alarm.preference.AlarmPreferencesActivity;
import com.example.the_spartan.toolkitv1.alarm.service.AlarmServiceBroadcastReciever;

import java.util.List;

/**
 * Created by the_spartan on 11/1/17.
 */

public class XtremeAlarmFragment extends Fragment {

    ListView mathAlarmListView;
    AlarmListAdapter alarmListAdapter;
    private static View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("ONCREATE", "GOT IT");



    //    setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("ONCREATE VIEW", "GOT IT");
        view = inflater.inflate(R.layout.xtreme_alarm_fragment, container, false);

        mathAlarmListView = view.findViewById(R.id.list);
        mathAlarmListView.setLongClickable(true);
        mathAlarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                final Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Delete");
                dialog.setMessage("Delete this alarm?");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Database.init(getContext());
                        Database.deleteEntry(alarm);
                        callMathAlarmScheduleService();

                        updateAlarmList();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return true;
            }
        });

        callMathAlarmScheduleService();
   //     updateAlarmList();

        alarmListAdapter = new AlarmListAdapter(getContext());
        updateAlarmList();
        mathAlarmListView.setAdapter(alarmListAdapter);
        mathAlarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
                Intent intent = new Intent(getContext(), AlarmPreferencesActivity.class);
                intent.putExtra("alarm", alarm);
                startActivity(intent);
            }

        });

        ImageButton addButton = view.findViewById(R.id.new_alarm_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAlarmIntent = new Intent(getContext(), AlarmPreferencesActivity.class);
                startActivity(newAlarmIntent);
            }
        });

        return view;
    }

    public void callMathAlarmScheduleService() {
        Intent mathAlarmServiceIntent = new Intent(getContext(), AlarmServiceBroadcastReciever.class);
        getContext().sendBroadcast(mathAlarmServiceIntent, null);
    }

    public void updateAlarmList() {
        Database.init(getContext());
        final List<Alarm> alarms = Database.getAll();
        alarmListAdapter.setMathAlarms(alarms);

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.v("RUNNINGONUI", "DOING");
                alarmListAdapter.notifyDataSetChanged();
                if (alarms.size() > 0) {
                    view.findViewById(R.id.empty).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.empty_image).setVisibility(View.INVISIBLE);
                } else {
                    view.findViewById(R.id.empty).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.empty_image).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateAlarmList();
        Log.v("onResume","nailed it!");
    }
}

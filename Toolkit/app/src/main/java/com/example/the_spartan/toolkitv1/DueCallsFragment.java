package com.example.the_spartan.toolkitv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.the_spartan.toolkitv1.Notification;

/**
 * Created by the_spartan on 11/1/17.
 */

public class DueCallsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.due_calls_preferences);

        Preference dueCallsSwitch = (Preference)findPreference("due_calls_switch");
        dueCallsSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isOn = (boolean)newValue;
                if (isOn){
                    Intent intent = new Intent(getContext(), com.example.the_spartan.toolkitv1.Notification.class);
                    getContext().startService(intent);
                } else{
                    Notification.isThreadRunning = false;
                    getContext().stopService(new Intent(getContext(), com.example.the_spartan.toolkitv1.Notification.class));
                }

                return true;
            }
        });

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

}
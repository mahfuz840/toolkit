package com.example.the_spartan.toolkitv1;

/**
 * Created by the_spartan on 11/1/17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
/**
 * Created by the_spartan on 11/1/17.
 */

/*public class SilentBreakerFragment extends Fragment {
    public static int isTurnedOn;
    View view;
    boolean isInputDone;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    EditText numberOfCallsInput;
    EditText timeIntervalInput;
    EditText specialNumberInput;
    Switch s;

    public SilentBreakerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInputDone = false;
        prefs = getContext().getSharedPreferences("MY_PREFERENCES",Context.MODE_PRIVATE);
        editor = prefs.edit();
        isTurnedOn = prefs.getInt("switch",0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.silent_breaker_fragment, container, false);

        numberOfCallsInput = (EditText) view.findViewById(R.id.minimum_number_of_calls_input);
        numberOfCallsInput.setText(prefs.getString("number_of_calls",null));
        timeIntervalInput = (EditText) view.findViewById(R.id.time_interval_input);
        timeIntervalInput.setText(prefs.getString("interval",null));
        specialNumberInput = (EditText)view.findViewById(R.id.special_number_input);
        specialNumberInput.setText(prefs.getString("special_number",null));

        s = (Switch) view.findViewById(R.id.switch1);
        if(isTurnedOn == 1){
            s.setChecked(true);
        } else if (isTurnedOn == 0){
            s.setChecked(false);
        }
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    editor.putInt("switch",1);
                    editor.commit();
                    if (isInputDone) {
                        isTurnedOn = 1;
                        editor.putInt("isTurnedOn",1);
                        editor.commit();
                        Toast.makeText(getContext(), "Silent Breaker Mode On", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Please set the inputs first", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editor.putInt("switch",0);
                    isTurnedOn = 0;
                    Toast.makeText(getContext(), "Silent Breaker Mode Off", Toast.LENGTH_SHORT).show();
                    editor.putInt("count",0);
                    editor.commit();
                }
            }
        });

        Button button = (Button)view.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInputs();
            }
        });

        return view;
    }

    public void setInputs() {
        String numberOfCallsString = numberOfCallsInput.getText().toString();

        if (numberOfCallsString.equals("")) {
            numberOfCallsInput.setError("this field cannot be empty");
        } else {
            int numberOfCalls = Integer.parseInt(numberOfCallsString);
        //    Variables.numberOfCalls = numberOfCalls;
            editor.putString("number_of_calls",numberOfCallsString);
            Toast.makeText(getContext(), "minimum number of calls set to " + numberOfCalls, Toast.LENGTH_SHORT).show();
        }

        String timeIntervalString = timeIntervalInput.getText().toString();

        if (timeIntervalString.equals("")) {
            timeIntervalInput.setError("this field cannot be empty");
        } else {
            int interval = Integer.parseInt(timeIntervalString);
        //    Variables.interval = interval;
            editor.putString("interval",timeIntervalString);
        }

        if (!timeIntervalString.equals("") && !numberOfCallsString.equals("")) {
            isInputDone = true;
        }

        String specialNumberString = specialNumberInput.getText().toString();
        editor.putString("special_number",specialNumberString);
        if (!specialNumberString.equals("")){
            int specialNumber = Integer.parseInt(specialNumberString);
            Variables.specialNumber = specialNumberString;
        }

        editor.commit();
    }
} */

public class SilentBreakerFragment extends PreferenceFragmentCompat{
    private static boolean silentBreakerSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference switchPreference = (Preference)findPreference("silent_breaker_switch");
        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isOn = (boolean)newValue;
                if (isOn){
                    silentBreakerSwitch = true;
                } else {
                    silentBreakerSwitch = false;
                }
                return true;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
}


package com.example.the_spartan.toolkitv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by the_spartan on 11/29/17.
 */

public class BootListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isDueCallsOn = preference.getBoolean("due_calls_switch",true);
        if (isDueCallsOn){
            Intent intent1 = new Intent(context,Notification.class);
            context.startService(intent1);
        }
    }
}

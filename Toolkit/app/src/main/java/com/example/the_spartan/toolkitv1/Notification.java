package com.example.the_spartan.toolkitv1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;

import com.example.the_spartan.toolkitv1.data.MissedCallProvider;
import com.example.the_spartan.toolkitv1.data.MissedCallContract.MissedCallEntry;

/**
 * Created by the_spartan on 11/29/17.
 */

public class Notification extends Service {
    SharedPreferences preferences;
    int interval;
    Thread thread;
    boolean isSwitchOn;
    public static boolean isThreadRunning;
    private static Notification mInstance = null;
    private static Thread mInstanceOfThread = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        interval = Integer.parseInt(preferences.getString("due_calls_interval", "5"));
        isSwitchOn = preferences.getBoolean("due_calls_switch",true);
        isThreadRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isThreadRunning) {
                    try {
                        Thread.sleep( interval * 60 * 1000);
                        String[] projection = {
                                MissedCallEntry.COLUMN_ID,
                                MissedCallEntry.COLUMN_NUMBER,
                                MissedCallEntry.COLUMN_TIME,
                                MissedCallEntry.COLUMN_CHECKED
                        };

                        Cursor cursor = getContentResolver().query(MissedCallProvider.CONTENT_URI,projection,null,null,null);
                        if (cursor.getCount() != 0 && cursor.getCount() > 0){
                            sendNotification();
                            cursor.close();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }

    private void sendNotification(){
        System.out.println("TRYING TO SEND NOTIFICATION");
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,"101");
        notification.setSmallIcon(R.drawable.due_calls_icon_edited);
        notification.setTicker("You have unchecked missed call(s)");
        notification.setContentTitle("You have unchecked missed calls");
        notification.setContentText("Tap to see them!");
        notification.setAutoCancel(true);
        notification.setDefaults(android.app.Notification.DEFAULT_SOUND);


        Intent intent = new Intent(this,DueCallsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,(int)System.currentTimeMillis(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101,notification.build());
    }

    public static Notification getmInstance() {
        return mInstance;
    }

    public static Thread getmInstanceOfThread() {
        return mInstanceOfThread;
    }
}

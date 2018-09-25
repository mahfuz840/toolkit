package com.example.the_spartan.toolkitv1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.the_spartan.toolkitv1.data.MissedCallContract;
import com.example.the_spartan.toolkitv1.data.MissedCallContract.MissedCallEntry;
import com.example.the_spartan.toolkitv1.data.MissedCallDbHelper;
import com.example.the_spartan.toolkitv1.data.MissedCallProvider;

public class DueCallsActivity extends AppCompatActivity {
    ListView listView;
    String[] projection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_calls);

        displayDatabaseInfo();


        Notification.isThreadRunning = false;

        Intent intent = new Intent(this,Notification.class);
        getBaseContext().stopService(intent);
    }

    private void displayDatabaseInfo() {
        projection = new String[]{
                MissedCallEntry.COLUMN_ID,
                MissedCallEntry.COLUMN_NUMBER,
                MissedCallEntry.COLUMN_TIME,
                MissedCallEntry.COLUMN_CHECKED
        };

        Cursor cursor = getContentResolver().query(MissedCallProvider.CONTENT_URI
                , projection
                , null
                , null
                , MissedCallEntry.COLUMN_ID + " DESC");

        MissedCallsCursorAdapter adapter = new MissedCallsCursorAdapter(this, cursor);
        listView = (ListView) findViewById(R.id.due_calls_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView numberView = (TextView) view.findViewById(R.id.number_text);
                String number = numberView.getText().toString();
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    String phoneNumber = getPhoneNumber(number,getBaseContext());
                    if (phoneNumber == null){
                        startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + number)));
                    } else {
                        startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phoneNumber)));
                    }
                } else{
                    Toast.makeText(DueCallsActivity.this, "Permission Denied to make calls", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getPhoneNumber(String name, Context context) {
        String number = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            number = c.getString(0);
        }
        c.close();
        return number;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_due_calls,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_missed_calls_menu:
                MissedCallDbHelper helper = new MissedCallDbHelper(getBaseContext());
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = getContentResolver().query(MissedCallProvider.CONTENT_URI,projection,null,null,null);
                if (cursor.getCount() == 0){
                    break;
                }
                cursor.close();
            //    int id = getContentResolver().delete(MissedCallProvider.CONTENT_URI,null,null);
                db.delete(MissedCallEntry.TABLE_NAME,null,null);
                displayDatabaseInfo();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

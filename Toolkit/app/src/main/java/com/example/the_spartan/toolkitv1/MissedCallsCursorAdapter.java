package com.example.the_spartan.toolkitv1;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.the_spartan.toolkitv1.data.MissedCallContract;
import com.example.the_spartan.toolkitv1.data.MissedCallContract.MissedCallEntry;

/**
 * Created by the_spartan on 11/29/17.
 */

public class MissedCallsCursorAdapter extends CursorAdapter {
    public MissedCallsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView numberView = (TextView)view.findViewById(R.id.number_text);
        TextView timeView = (TextView)view.findViewById(R.id.time_text);

        String number = cursor.getString(cursor.getColumnIndex(MissedCallEntry.COLUMN_NUMBER));
        String time = cursor.getString(cursor.getColumnIndex(MissedCallEntry.COLUMN_TIME));

        String contactName = getContactName(number,context);
        if (contactName == null){
            numberView.setText(number);
        } else{
            numberView.setText(contactName);
        }
        timeView.setText(time);
    }

    public String getContactName( String phoneNumber, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }


}

package com.example.the_spartan.toolkitv1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.the_spartan.toolkitv1.data.MissedCallContract.MissedCallEntry;

/**
 * Created by the_spartan on 11/20/17.
 */

public class MissedCallProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "com.android.example.missedcalls";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MISSED_CALLS = "missed_calls";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_MISSED_CALLS);

    public static final UriMatcher sUrimatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int MISSED_CALLS = 100;
    public static final int MISSED_CALL_ID = 101;

    static {
        sUrimatcher.addURI(CONTENT_AUTHORITY,PATH_MISSED_CALLS,MISSED_CALLS);
        sUrimatcher.addURI(CONTENT_AUTHORITY,PATH_MISSED_CALLS + "/#",MISSED_CALL_ID);
    }

    MissedCallDbHelper helper;

    @Override
    public boolean onCreate() {
        helper = new MissedCallDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        int match = sUrimatcher.match(uri);
        switch (match){
            case MISSED_CALLS:
                cursor = db.query(MissedCallEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                break;
            case MISSED_CALL_ID:
                selection = MissedCallEntry._ID + "= ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MissedCallEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                break;
            default:
                throw new IllegalArgumentException("Cannot query on an invalid URI");

        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String number = values.getAsString(MissedCallEntry.COLUMN_NUMBER);
        if(number == null){
            throw new IllegalArgumentException("CAnnot create database entry due to empty number");
        }

        String time = values.getAsString(MissedCallEntry.COLUMN_TIME);
        if(time == null){
            throw new IllegalArgumentException("CAnnot create database entry due to invalid time");
        }

        int isChecked = values.getAsInteger(MissedCallEntry.COLUMN_CHECKED);

        int match = sUrimatcher.match(uri);
        switch (match){
            case MISSED_CALLS:
                return insertMissedCalls(uri,values);
            default:
                throw new IllegalArgumentException("Cannot insert item into database");
        }
    }

    @Override
    public int delete(Uri uri,String selection,String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int match = sUrimatcher.match(uri);
        switch (match){
            case MISSED_CALLS:
                db.delete(MissedCallEntry.TABLE_NAME,null,null);
            case MISSED_CALL_ID:
                selection = MissedCallEntry._ID + " = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                System.out.println("ID = " + selectionArgs[0]);
                long deletedRow = db.delete(MissedCallEntry.TABLE_NAME,selection,selectionArgs);
                System.out.println("DELETE RESULT " + deletedRow);
        }
        return 0;
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
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private Uri insertMissedCalls(Uri uri,ContentValues values){
        SQLiteDatabase db = helper.getWritableDatabase();

        long newRowId = db.insert(MissedCallEntry.TABLE_NAME,null,values);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE NAME = '"+MissedCallEntry.TABLE_NAME+"'");
        return ContentUris.withAppendedId(MissedCallProvider.CONTENT_URI,newRowId);

    }
}

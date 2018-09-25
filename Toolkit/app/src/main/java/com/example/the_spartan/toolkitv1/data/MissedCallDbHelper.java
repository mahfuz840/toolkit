package com.example.the_spartan.toolkitv1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.the_spartan.toolkitv1.data.MissedCallContract.MissedCallEntry;

/**
 * Created by the_spartan on 11/20/17.
 */

public class MissedCallDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "missedCalls.db";
    public static final int DATABASE_VERSION = 1;
    public MissedCallDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE =
                "CREATE TABLE " + MissedCallEntry.TABLE_NAME + "("
                        + MissedCallEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MissedCallEntry.COLUMN_NUMBER + " TEXT NOT NULL, "
                        + MissedCallEntry.COLUMN_TIME + " TEXT, "
                        + MissedCallEntry.COLUMN_CHECKED + " INTEGER NOT NULL DEFAULT 0)";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

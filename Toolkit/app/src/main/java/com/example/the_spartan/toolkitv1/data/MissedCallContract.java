package com.example.the_spartan.toolkitv1.data;

import android.provider.BaseColumns;

/**
 * Created by the_spartan on 11/20/17.
 */

public class MissedCallContract {
    private MissedCallContract(){

    }

    public static final class MissedCallEntry implements BaseColumns {
        public static final String TABLE_NAME = "missedCalls";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_CHECKED = "isChecked";
    }
}

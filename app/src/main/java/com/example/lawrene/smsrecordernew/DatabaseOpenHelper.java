package com.example.lawrene.smsrecordernew;


import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

//This class opens the databnse
public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "smsdb.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
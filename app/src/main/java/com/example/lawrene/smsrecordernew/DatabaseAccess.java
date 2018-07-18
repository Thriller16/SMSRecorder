package com.example.lawrene.smsrecordernew;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

//The database class
public class DatabaseAccess {
    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    //First constructor
    public DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    //Second constructor
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }


    //This populates the mainActivity
    public List<SMSData> getSMS() {
        List<SMSData> smsDataList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM smsTable", null);
        cursor.moveToFirst();

        while (!(cursor.isAfterLast())) {

            smsDataList.add(new SMSData(cursor.getString(cursor.getColumnIndex("phoneNumber")),
                    cursor.getString(cursor.getColumnIndex("message")),
                    cursor.getString(cursor.getColumnIndex("status"))));

            cursor.moveToNext();
        }
        cursor.close();

        return smsDataList;
    }


    //This is for adding a new value to the favorites activity
    public boolean add(String phoneNumber, String message, String status) {
        try {
            //Adding a hymn to the favorites table
            ContentValues cv = new ContentValues();
            cv.put("phoneNumber", phoneNumber);
            cv.put("message", message);
            cv.put("status", status);

            database.insert("smsTable", null, cv);

//            //Updating the hymns table and changing the int from 1 to 0
//            ContentValues ccv = new ContentValues();
//            ccv.put("Field9", 1);
//            database.update("Hymns", ccv, "Title =" + "'" + title + "'", null);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Delete a value from the database
    public boolean deleteNote(String note) {
        try {
            database.delete("Notes", "note =" + "'" + note + "'", null);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(String message) {
            try {
                ContentValues cv = new ContentValues();
                cv.put("status", "uploaded");
                database.update("smsTable", cv, "message LIKE" + "'%" + message + "%'", null);
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
    }
//
//    public boolean deleteAll() {
//        try {
//            database.delete("Favourites", "Title", null);
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean storeFont(int fontIndex) {
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put("Font", fontIndex);
//            database.update("Settings", cv, null, null);
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public int getFont() {
//        Cursor cursor = database.rawQuery("SELECT Font FROM Settings", null);
//        cursor.moveToFirst();
//        int fontIndex = cursor.getInt(cursor.getColumnIndex("Font"));
//        cursor.moveToNext();
//        cursor.close();
//        return fontIndex;
//    }
//
//    public boolean storeTextSize(int sizeIndex) {
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put("TextSize", sizeIndex);
//            database.update("Settings", cv, null, null);
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean storeDefaultTextSize() {
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put("TextSize", 2);
//            database.update("Settings", cv, null, null);
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public int getTextSize() {
//        Cursor cursor = database.rawQuery("SELECT TextSize FROM Settings", null);
//        cursor.moveToFirst();
//        int sizeIndex = cursor.getInt(cursor.getColumnIndex("TextSize"));
//        cursor.moveToNext();
//        cursor.close();
//        return sizeIndex;
//    }
//
//    public boolean storeAppTheme(int themeIndex) {
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put("Theme", themeIndex);
//            database.update("Settings", cv, null, null);
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public int getAppTheme() {
//        Cursor cursor = database.rawQuery("SELECT Theme FROM Settings", null);
//        cursor.moveToFirst();
//        int sizeIndex = cursor.getInt(cursor.getColumnIndex("Theme"));
//        cursor.moveToNext();
//        cursor.close();
//        return sizeIndex;
//    }
}

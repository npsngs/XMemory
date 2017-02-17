package com.forthe.xmemory.frame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
        super(context, "CTS.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS CTM(" +
                "ce VARCHAR(10) NOT NULL," +
                "tn VARCHAR(50) NOT NULL," +
                "som VARCHAR NOT NULL," +
                "PRIMARY KEY(ce, tn))");
        db.execSQL("CREATE TABLE IF NOT EXISTS SOD(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "sod_l VARCHAR NOT NULL UNIQUE ON CONFLICT FAIL,"+
                "sod_c VARCHAR NOT NULL UNIQUE ON CONFLICT FAIL,"+
                "sod_r VARCHAR NOT NULL UNIQUE ON CONFLICT FAIL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

package com.forthe.xmemory.frame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.forthe.xmemory.core.CTS;
import com.forthe.xmemory.core.CTSStore;

import java.util.ArrayList;
import java.util.List;


public class CTSStoreImpl implements CTSStore {
    private DBHelper dbHelper;

    public CTSStoreImpl(Context context) {
        dbHelper = new DBHelper(context);
    }

    @Override
    public void insert(CTS cts) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        StringBuilder sb = new StringBuilder("INSERT OR FAIL INTO CTM(ce, tn, som) VALUES('");
        sb
                .append(cts.getCE()).append("','")
                .append(cts.getTN()).append("','")
                .append(cts.getSOM()).append("');");
        db.execSQL(sb.toString());
    }

    @Override
    public List<CTS> query() {
        List<CTS> ctsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CTM;", null);
        while (cursor.moveToNext()){
            CTS cts = new CTS();
            cts.setCE(cursor.getString(0));
            cts.setTN(cursor.getString(1));
            cts.setSOM(cursor.getString(2));
            ctsList.add(cts);
        }
        cursor.close();
        return ctsList;
    }

    @Override
    public List<CTS> queryByCE(String CE) {
        List<CTS> ctsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM CTM WHERE ce='%s';", CE), null);
        while (cursor.moveToNext()){
            CTS cts = new CTS();
            cts.setCE(cursor.getString(0));
            cts.setTN(cursor.getString(1));
            cts.setSOM(cursor.getString(2));
            ctsList.add(cts);
        }
        cursor.close();
        return ctsList;
    }

    @Override
    public List<CTS> queryByTN(String TN) {
        List<CTS> ctsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM CTM WHERE tn='%s';", TN), null);
        while (cursor.moveToNext()){
            CTS cts = new CTS();
            cts.setCE(cursor.getString(0));
            cts.setTN(cursor.getString(1));
            cts.setSOM(cursor.getString(2));
            ctsList.add(cts);
        }
        cursor.close();
        return ctsList;
    }

    @Override
    public List<CTS> queryByCETN(String CE, String TN) {
        List<CTS> ctsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM CTM WHERE ce='%s'AND tn='%s';", CE, TN), null);
        while (cursor.moveToNext()){
            CTS cts = new CTS();
            cts.setCE(cursor.getString(0));
            cts.setTN(cursor.getString(1));
            cts.setSOM(cursor.getString(2));
            ctsList.add(cts);
        }
        cursor.close();
        return ctsList;
    }

}

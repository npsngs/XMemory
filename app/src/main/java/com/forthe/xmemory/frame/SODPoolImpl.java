package com.forthe.xmemory.frame;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.forthe.xmemory.core.Const;
import com.forthe.xmemory.core.RandomStr;
import com.forthe.xmemory.core.SODPool;
import com.forthe.xmemory.core.SOTransverter;


public class SODPoolImpl implements SODPool {
    private DBHelper dbHelper;
    private SOTransverter transverter;
    private RandomStr randomStr;
    public SODPoolImpl(Context context) {
        dbHelper = new DBHelper(context);
    }

    @Override
    public boolean hasInited() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SOD LIMIT 1;", null);
        boolean hasInit = cursor.moveToNext();
        cursor.close();
        return hasInit;
    }

    @Override
    public String createRandomSOD() {
        if(null == transverter)transverter = new SOTransverterImpl();
        if(null == randomStr)randomStr = new RandomStrImpl();
        String so = randomStr.createRandomStr(16, 6);
        String key = randomStr.createRandomStr(Const.KEY_MIN_LEN);
        try {
            return transverter.SO2SOD(so, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void fillPool() {
        int size = Const.SOD_POOLSIZE;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement statement = db.compileStatement("INSERT OR FAIL INTO SOD(sod_l, sod_c, sod_r) VALUES(?,?,?);");

        db.beginTransaction();
        try {
            for (int i = 0;i<size;i++){
                statement.clearBindings();
                String sod = createRandomSOD();
                if(sod == null){
                    throw new IllegalStateException("create sod fail");
                }

                String[] sos;
                try{
                    sos = transverter.SOD2SOS(sod);
                }catch (Exception e){
                    e.printStackTrace();
                    sos = null;
                }

                if(sos == null){
                    throw new IllegalStateException("create sos fail");
                }

                statement.bindString(1,sos[0]);
                statement.bindString(2,sos[1]);
                statement.bindString(3,sos[2]);
                statement.executeInsert();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void insert(String sod) {
        if(null == transverter)transverter = new SOTransverterImpl();
        String[] sos;
        try{
            sos = transverter.SOD2SOS(sod);
        }catch (Exception e){
            e.printStackTrace();
            sos = null;
        }

        if(sos == null){
            throw new IllegalStateException("create sos fail");
        }


        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            int index = (int) (Math.random()*Const.SOD_POOLSIZE);
            db.execSQL(String.format("UPDATE SOD SET sod_l='%s' WHERE id=%d;",sos[0],index));

            index = (int) (Math.random()*Const.SOD_POOLSIZE);
            db.execSQL(String.format("UPDATE SOD SET sod_c='%s' WHERE id=%d;",sos[1],index));

            index = (int) (Math.random()*Const.SOD_POOLSIZE);
            db.execSQL(String.format("UPDATE SOD SET sod_r='%s' WHERE id=%d;",sos[2],index));

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public String[][] queryColumns() {
        String[][] data = new String[Const.SOD_POOLSIZE][3];
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SOD;", null);
        int row = 0;
        while (cursor.moveToNext() && row < Const.SOD_POOLSIZE){
            data[row][0] = cursor.getString(1);
            data[row][1] = cursor.getString(2);
            data[row][2] = cursor.getString(3);
            row++;
        }
        return data;
    }
}

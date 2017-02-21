package com.forthe.xmemory.frame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.forthe.xmemory.core.ByteStr;
import com.forthe.xmemory.core.CTS;
import com.forthe.xmemory.core.CTSStore;
import com.forthe.xmemory.core.CodeConvertor;
import com.forthe.xmemory.core.Const;
import com.forthe.xmemory.core.RandomStr;
import com.forthe.xmemory.core.SOConvertor;
import com.forthe.xmemory.core.YETPool;

import java.util.ArrayList;
import java.util.List;


class DBHelper extends SQLiteOpenHelper implements CTSStore, YETPool{

    static void init(Context context){
        if (null == instance) {
            instance = new DBHelper(context.getApplicationContext());
        }
    }

    static DBHelper getInstance(){
        if(null == instance){
            throw new IllegalStateException(DBHelper.class.getName()+" not init");
        }
        return instance;
    }


    private static DBHelper instance;
    private DBHelper(Context context){
        super(context, "CTS.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS CTM(" +
                "ce VARCHAR(32) PRIMARY KEY NOT NULL," +
                "tn VARCHAR NOT NULL," +
                "ta VARCHAR(12) ," +
                "som VARCHAR(32) NOT NULL," +
                "svl INTEGER NOT NULL," +
                "sod VARCHAR NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS YET(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "yet_l VARCHAR NOT NULL,"+
                "yet_c VARCHAR NOT NULL,"+
                "yet_r VARCHAR NOT NULL)");

    }


    /*****************************************************
     *
     *  CTS ops
     *
     *****************************************************/


    @Override
    public void insert(CTS cts) {
        SQLiteDatabase db = getWritableDatabase();
        String sb = "INSERT OR FAIL INTO CTM(ce, tn, ta, som, svl, sod) VALUES('" +
                cts.getCE() + "','" +
                cts.getTN() + "','" +
                cts.getTA() + "','" +
                cts.getSOM() + "'," +
                cts.getSVL() + ",'" +
                cts.getSOD() + "');";
        db.execSQL(sb);
    }

    @Override
    public List<CTS> query() {
        List<CTS> ctsList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CTM;", null);
        while (cursor.moveToNext()){
            CTS cts = new CTS();
            cts.setCE(cursor.getString(0));
            cts.setTN(cursor.getString(1));
            cts.setTA(cursor.getString(2));
            cts.setSOM(cursor.getString(3));
            cts.setSVL(cursor.getInt(4));
            cts.setSOD(cursor.getString(5));
            ctsList.add(cts);
        }
        cursor.close();
        return ctsList;
    }


    private List<CTS> queryByCE(String CE) {
        List<CTS> ctsList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM CTM WHERE ce='%s';", CE), null);
        while (cursor.moveToNext()){
            CTS cts = new CTS();
            cts.setCE(cursor.getString(0));
            cts.setTN(cursor.getString(1));
            cts.setTA(cursor.getString(2));
            cts.setSOM(cursor.getString(3));
            cts.setSVL(cursor.getInt(4));
            cts.setSOD(cursor.getString(5));
            ctsList.add(cts);
        }
        cursor.close();
        return ctsList;
    }

    @Override
    public List<CTS> queryByTN(String TN) {
        SOConvertor convertor = new SOConvertorImpl();
        String ce = convertor.TNToCE(TN);
        return queryByCE(ce);
    }

    @Override
    public List<CTS> queryByTA(String TA) {
        List<CTS> ctsList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM CTM WHERE ta='%s';", TA), null);
        while (cursor.moveToNext()){
            CTS cts = new CTS();
            cts.setCE(cursor.getString(0));
            cts.setTN(cursor.getString(1));
            cts.setTA(cursor.getString(2));
            cts.setSOM(cursor.getString(3));
            cts.setSVL(cursor.getInt(4));
            cts.setSOD(cursor.getString(5));
            ctsList.add(cts);
        }
        cursor.close();
        return ctsList;
    }


    /********************************************************
     *
     *  YET ops
     *
     ********************************************************/

    @Override
    public boolean hasInited() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM YET LIMIT 1;", null);
        boolean hasInit = cursor.moveToNext();
        cursor.close();
        return hasInit;
    }



    @Override
    public String[] createRandomYET() {
        try {
            String[] ret = new String[3];
            for (int i=0;i<ret.length;i++){
                String key = randomStr.createRandomStr(9);
                byte[] bts = codeConvertor.encode(key.getBytes());
                ret[i] = byteStr.byteToStr(bts);
            }

            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ByteStr byteStr;
    private CodeConvertor codeConvertor;
    private RandomStr randomStr;
    @Override
    public void fillPool() {
        if(null == codeConvertor)codeConvertor = new ShiftCodeConvertor(4);
        if(null == byteStr)byteStr = new HexByteStr();
        if(null == randomStr)randomStr = new RandomStrImpl();

        int size = Const.YET_POOLSIZE;
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement statement = db.compileStatement("INSERT OR FAIL INTO YET(yet_l, yet_c, yet_r) VALUES(?,?,?);");

        db.beginTransaction();
        try {
            for (int i = 0;i<size;i++){
                statement.clearBindings();
                String[] yet = createRandomYET();
                if(yet == null){
                    throw new IllegalStateException("create yet fail");
                }

                statement.bindString(1,yet[0]);
                statement.bindString(2,yet[1]);
                statement.bindString(3,yet[2]);
                statement.executeInsert();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public String[][] queryColumns() {
        String[][] data = new String[Const.YET_POOLSIZE][3];
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM YET;", null);
        int row = 0;
        while (cursor.moveToNext() && row < Const.YET_POOLSIZE){
            data[row][0] = cursor.getString(1);
            data[row][1] = cursor.getString(2);
            data[row][2] = cursor.getString(3);
            row++;
        }
        cursor.close();
        return data;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

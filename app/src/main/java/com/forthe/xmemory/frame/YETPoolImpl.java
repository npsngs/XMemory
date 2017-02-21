package com.forthe.xmemory.frame;

import com.forthe.xmemory.core.YETPool;

public class YETPoolImpl implements YETPool {
    private DBHelper dbHelper;
    public YETPoolImpl() {
        dbHelper = DBHelper.getInstance();
    }

    @Override
    public boolean hasInited() {
        return dbHelper.hasInited();
    }

    @Override
    public String[] createRandomYET() {
        return dbHelper.createRandomYET();
    }


    @Override
    public void fillPool() {
        dbHelper.fillPool();
    }

    @Override
    public String[][] queryColumns() {
        return dbHelper.queryColumns();
    }
}

package com.forthe.xmemory.frame;

import com.forthe.xmemory.core.CTS;
import com.forthe.xmemory.core.CTSStore;
import java.util.List;

class CTSStoreImpl implements CTSStore {
    private DBHelper dbHelper;

    CTSStoreImpl() {
        dbHelper = DBHelper.getInstance();
    }

    @Override
    public void insert(CTS cts) {
        dbHelper.insert(cts);
    }

    @Override
    public List<CTS> query() {
        return dbHelper.query();
    }

    @Override
    public List<CTS> queryByTA(String TA) {
        return dbHelper.queryByTA(TA);
    }

    @Override
    public List<CTS> queryByTN(String TN) {
        return dbHelper.queryByTN(TN);
    }
}

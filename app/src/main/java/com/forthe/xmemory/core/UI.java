package com.forthe.xmemory.core;

import android.content.Context;

public interface UI {
    void init(Context context);
    void insert(String TN, String SO, int svl) throws Exception;
    void insert(String TN, String SO, int svl, String TA) throws Exception;
    String querySO(String TN) throws Exception;
    boolean testSO(String TN, String SO) throws Exception;
}

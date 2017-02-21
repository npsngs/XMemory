package com.forthe.xmemory.core;

import java.util.List;

public interface CTSStore {
    void insert(CTS cts);
    List<CTS> query();
    List<CTS> queryByTN(String TN);
    List<CTS> queryByTA(String TA);
}

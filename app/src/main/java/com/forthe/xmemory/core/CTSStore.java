package com.forthe.xmemory.core;

import java.util.List;

public interface CTSStore {
    void insert(CTS cts);
    List<CTS> query();
    List<CTS> queryByCE(String CE);
    List<CTS> queryByTN(String TN);
    List<CTS> queryByCETN(String CE,String TN);
}

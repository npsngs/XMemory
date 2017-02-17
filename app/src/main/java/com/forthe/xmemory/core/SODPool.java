package com.forthe.xmemory.core;

public interface SODPool {
    boolean hasInited();
    String createRandomSOD();
    void fillPool();
    void insert(String sod);
    String[][] queryColumns();
}

package com.forthe.xmemory.core;

public interface YETPool {
    boolean hasInited();
    String[] createRandomYET();
    void fillPool();
    String[][] queryColumns();
}

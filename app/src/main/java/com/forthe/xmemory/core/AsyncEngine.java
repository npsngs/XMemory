package com.forthe.xmemory.core;

public interface AsyncEngine {
    void startQuerySOD(String SOM, ResultCallback callback, ProgressListener progressListener);

    void startInsertSOD(String SOD, ResultCallback callback);

    void startFillPool(int size, ProgressListener progressListener);
}

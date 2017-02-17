package com.forthe.xmemory.core;

public interface UI {
    void insert(String CE, String TN, String SO) throws Exception;
    String querySO(String CE, String TN) throws Exception;
    boolean testSO(String CE, String TN, String SO) throws Exception;
}

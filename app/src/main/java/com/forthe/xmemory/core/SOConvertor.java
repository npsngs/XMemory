package com.forthe.xmemory.core;

public interface SOConvertor {
    void encode(CTS cts, String so) throws Exception;
    String decode(CTS cts) throws Exception;
    String SOToSOM(CTS cts, String so) throws Exception;
    String TNToCE(String TN);
}

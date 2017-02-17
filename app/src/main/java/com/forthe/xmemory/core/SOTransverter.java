package com.forthe.xmemory.core;

public interface SOTransverter {
    String SOS2SOD(String[] sos) throws Exception;
    String[] SOD2SOS(String sod) throws Exception;
    String SOD2SOM(String sod, String key) throws Exception;
    String SOD2SO(String sod, String key) throws Exception;
    String SO2SOD(String so, String key) throws Exception;
    String SO2SOM(String so, String key) throws Exception;
    String CETN2Key(String CE, String TN);
    String MIXString(String... str);
}

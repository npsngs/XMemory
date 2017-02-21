package com.forthe.xmemory.core;

public class CTS {
    private String TA;//tag
    private String CE;//name-md5
    private String TN;//name
    private String SOM;//value
    private int SVL;//security level
    private String SOD;

    public String getTA() {
        return null==TA?"":TA;
    }

    public void setTA(String TA) {
        this.TA = TA;
    }

    public String getSOD() {
        return SOD;
    }

    public void setSOD(String SOD) {
        this.SOD = SOD;
    }

    public String getCE() {
        return CE;
    }

    public void setCE(String CE) {
        this.CE = CE;
    }

    public String getSOM() {
        return SOM;
    }

    public void setSOM(String SOM) {
        this.SOM = SOM;
    }

    public String getTN() {
        return TN;
    }

    public void setTN(String TN) {
        this.TN = TN;
    }

    public int getSVL() {
        return SVL;
    }

    public void setSVL(int SVL) {
        this.SVL = SVL;
    }

    @Override
    public String toString() {
        return "CTS{\n" +
                "CE='" + CE + '\'' +
                ",\nTN='" + TN + '\'' +
                ",\nTA='" + TA + '\'' +
                ",\nSOM='" + SOM + '\'' +
                ",\nSVL=" + SVL +
                ",\nSOD='" + SOD + '\'' +
                '}';
    }
}

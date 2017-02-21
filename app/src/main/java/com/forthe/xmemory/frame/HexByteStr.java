package com.forthe.xmemory.frame;

import com.forthe.xmemory.core.ByteStr;

public class HexByteStr implements ByteStr {
    @Override
    public String byteToStr(byte[] src) {
        return byteToHexString(src);
    }

    @Override
    public byte[] strToByte(String src) {
        return hexStringToByte(src);
    }


    public String byteToHexString(byte[] src) {
        if(null == src || src.length < 1){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for(byte b:src){
            int i = b&0xff;
            if(i < 16){
                sb.append("0").append(Integer.toHexString(i));
            }else{
                sb.append(Integer.toHexString(i));
            }
        }
        return sb.toString();
    }


    public byte[] hexStringToByte(String src) {
        if(null == src || ""== src || src.length()%2!=0){
            return null;
        }
        byte[] ret = new byte[src.length()/2];
        for(int i=0;i<ret.length;i++){
            int b = Integer.parseInt(src.substring(i*2, i*2+2), 16);
            ret[i] = (byte) b;
        }
        return ret;
    }
}

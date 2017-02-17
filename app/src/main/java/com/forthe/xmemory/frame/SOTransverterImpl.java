package com.forthe.xmemory.frame;

import android.text.TextUtils;

import com.forthe.xmemory.core.BASE64;
import com.forthe.xmemory.core.Const;
import com.forthe.xmemory.core.DES;
import com.forthe.xmemory.core.MD5;
import com.forthe.xmemory.core.Reversal;
import com.forthe.xmemory.core.SOTransverter;


public class SOTransverterImpl implements SOTransverter {

    @Override
    public String SOS2SOD(String[] sos) throws Exception {

        byte[] ret1 = sos[0].getBytes();
        byte[] ret2 = sos[1].getBytes();
        byte[] ret3 = sos[2].getBytes();

        BASE64 base64 = new BASE64Impl();
        ret1 = base64.decode(ret1);
        ret2 = base64.decode(ret2);
        ret3 = base64.decode(ret3);

        DES des = new DESImpl();
        ret1 = des.decrypt(ret1, Const.KEY);
        ret2 = des.decrypt(ret2, Const.KEY);
        ret3 = des.decrypt(ret3, Const.KEY);

        byte[] ret = new byte[ret1.length+ret2.length+ret3.length];
        int pos1=0;
        int pos2=0;
        int pos3=0;
        int index;
        for(int i=0;i<ret.length;i++){
            index = i%3;
            switch (index){
                case 0:
                    if(pos1 < ret1.length){
                        ret[i] = ret1[pos1];
                        pos1++;
                        break;
                    }
                case 1:
                    if(pos2 < ret2.length){
                        ret[i] = ret2[pos2];
                        pos2++;
                        break;
                    }
                case 2:
                    if(pos3 < ret3.length){
                        ret[i] = ret3[pos3];
                        pos3++;
                        break;
                    }
            }
        }

        return new String(ret);
    }

    @Override
    public String[] SOD2SOS(String sod) throws Exception {
        byte[] ret = sod.getBytes();
        int subLen = ret.length/3;

        byte[] ret1 = new byte[subLen];
        byte[] ret2 = new byte[subLen];
        byte[] ret3 = new byte[ret.length - 2*subLen];

        int pos1=0;
        int pos2=0;
        int pos3=0;
        int index;
        for(int i=0;i<ret.length;i++){
            index = i%3;
            switch (index){
                case 0:
                    if(pos1 < ret1.length){
                        ret1[pos1] = ret[i];
                        pos1++;
                        break;
                    }
                case 1:
                    if(pos2 < ret2.length){
                        ret2[pos2] = ret[i];
                        pos2++;
                        break;
                    }
                case 2:
                    if(pos3 < ret3.length){
                        ret3[pos3] = ret[i];
                        pos3++;
                        break;
                    }
            }
        }
        DES des = new DESImpl();
        ret1 = des.encrypt(ret1, Const.KEY);
        ret2 = des.encrypt(ret2, Const.KEY);
        ret3 = des.encrypt(ret3, Const.KEY);

        BASE64 base64 = new BASE64Impl();
        ret1 = base64.encode(ret1);
        ret2 = base64.encode(ret2);
        ret3 = base64.encode(ret3);

        String[] sos = new String[]{
                new String(ret1),new String(ret2),new String(ret3)
        };
        return sos;
    }

    @Override
    public String SOD2SOM(String sod, String key) throws Exception {
        MD5 md5 = new MD5Impl();
        return md5.md5(MIXString(sod, key, sod));
    }

    @Override
    public String SOD2SO(String sod, String key) throws Exception {
        DES des = new DESImpl();
        BASE64 base64 = new BASE64Impl();
        if(TextUtils.isEmpty(sod) || TextUtils.isEmpty(key) || key.length() < Const.KEY_MIN_LEN){
            throw new IllegalArgumentException("SO2SOD Argument error");
        }
        return new String(des.decrypt(base64.decode(sod.getBytes()), key));
    }

    @Override
    public String SO2SOD(String so, String key) throws Exception {
        DES des = new DESImpl();
        BASE64 base64 = new BASE64Impl();
        if(TextUtils.isEmpty(so) || TextUtils.isEmpty(key) || key.length() < Const.KEY_MIN_LEN){
            throw new IllegalArgumentException("SO2SOD Argument error");
        }
        return new String(base64.encode(des.encrypt(so.getBytes(), key)));
    }

    @Override
    public String SO2SOM(String so, String key) throws Exception {
        MD5 md5 = new MD5Impl();
        String sod = SO2SOD(so, key);
        return md5.md5(MIXString(sod, key, sod));
    }

    @Override
    public String CETN2Key(String CE, String TN) {
        if(TextUtils.isEmpty(CE) && TextUtils.isEmpty(TN)){
            return null;
        }

        Reversal rever = new ReversalImpl();
        if(TextUtils.isEmpty(CE)){
            while (TN.length() < Const.KEY_MIN_LEN){
                TN = TN.concat(rever.reversal(TN));
            }
            TN = rever.reversal(TN);
            return TN.substring(0, 8);
        }

        if(TextUtils.isEmpty(TN)){
            while (CE.length() < Const.KEY_MIN_LEN){
                CE = rever.reversal(CE).concat(CE);
            }
            CE = rever.reversal(CE);
            return CE.substring(0, 8);
        }

        String tmp = rever.reversal(CE).concat(TN);
        while (tmp.length() < Const.KEY_MIN_LEN){
            tmp = tmp.concat(rever.reversal(tmp));
        }
        return tmp.substring(0, 8);
    }

    @Override
    public String MIXString(String... str) {
        return new MixImpl().mix(str);
    }
}

package com.forthe.xmemory.frame;

import com.forthe.xlog.XLog;
import com.forthe.xmemory.core.BASE64;
import com.forthe.xmemory.core.CTS;
import com.forthe.xmemory.core.CodeConvertor;
import com.forthe.xmemory.core.Const;
import com.forthe.xmemory.core.DES;
import com.forthe.xmemory.core.Iterator;
import com.forthe.xmemory.core.KeyFactory;
import com.forthe.xmemory.core.MD5;
import com.forthe.xmemory.core.Mix;
import com.forthe.xmemory.core.Reversal;
import com.forthe.xmemory.core.SOConvertor;


public class SOConvertorImpl implements SOConvertor {
    private KeyFactory keyFactory;
    private DES des;
    private BASE64 base64;
    public SOConvertorImpl() {
        keyFactory = new KeyFactoryImpl();
        base64 = new BASE64Impl();
        des = new DESImpl();
    }


    @Override
    public void encode(CTS cts, String so) throws Exception {
        switch (cts.getSVL()){
            case Const.SVL_1:
                cts.setSOD(encodeLevelLow(cts,so));
                break;
            case Const.SVL_2:
                cts.setSOD(encodeLevelMedium(cts,so));
                break;
            case Const.SVL_3:
                cts.setSOD(encodeLevelHigh(cts, so));
                break;
        }
    }


    @Override
    public String decode(CTS cts) throws Exception {
        switch (cts.getSVL()){
            case Const.SVL_1:
                return decodeLevelLow(cts, cts.getSOD());
            case Const.SVL_2:
                return decodeLevelMedium(cts, cts.getSOD());
            case Const.SVL_3:
                return decodeLevelHigh(cts, cts.getSOD());
        }
        throw new IllegalStateException("illegal svl value");
    }








    @Override
    public String SOToSOM(CTS cts, String so) throws Exception {
        MD5 md5 = new MD5Impl();
        Reversal reversal = new ReversalImpl();
        String ec = reversal.reversal(cts.getCE());
        String nt = reversal.reversal(cts.getTN());
        String som = md5.md5(so);
        Mix mix = new MixImpl();
        som = mix.mix(som, nt, ec);
        som = md5.md5(som);
        return som;
    }

    @Override
    public String TNToCE(String TN) {
        MD5 md5 = new MD5Impl();
        Reversal reversal = new ReversalImpl();
        String nt = reversal.reversal(TN);
        return md5.md5(nt);
    }

    private String encodeLevelLow(CTS cts,String src) throws Exception {
        CodeConvertor codeConvertor = new ShiftCodeConvertor(cts.getSOM().charAt(cts.getSOM().length()-2));
        String key = keyFactory.getFixedKey();
        byte[] ret = des.encrypt(src.getBytes(), key);
        ret = codeConvertor.encode(ret);
        ret = base64.encode(ret);
        return new String(ret);
    }


    private String encodeLevelMedium(CTS cts, String src) throws Exception {
        CodeConvertor codeConvertor = new ShiftCodeConvertor(cts.getSOM().getBytes()[10]);
        String fixKey = keyFactory.getFixedKey();
        String dynamicKey = keyFactory.getDynamicKey(cts.getCE(),cts.getTN(),cts.getSOM());
        byte[] ret = des.encrypt(src.getBytes(), dynamicKey);
        ret = codeConvertor.encode(ret);
        ret = des.encrypt(ret, fixKey);
        ret = base64.encode(ret);
        return new String(ret);
    }



    private String encodeLevelHigh(CTS cts, String src) throws Exception {
        CodeConvertor codeConvertor = new ShiftCodeConvertor(cts.getSOM().getBytes()[3]);
        String fixKey = keyFactory.getFixedKey();
        String dynamicKey = keyFactory.getDynamicKey(cts.getCE(),cts.getTN());
        String randomKey = keyFactory.getRestrictedRandomKey();

        byte[] ret = des.encrypt(src.getBytes(), randomKey);
        ret  = codeConvertor.encode(ret);
        ret = des.encrypt(ret, fixKey);
        ret  = codeConvertor.encode(ret);
        ret = des.encrypt(ret, dynamicKey);
        ret  = codeConvertor.encode(ret);
        ret = base64.encode(ret);
        return new String(ret);
    }



    private String decodeLevelHigh(CTS cts, String src) throws Exception {
        Iterator<String> iterator = new YETIterator();
        CodeConvertor codeConvertor = new ShiftCodeConvertor(cts.getSOM().getBytes()[3]);
        String fixKey = keyFactory.getFixedKey();
        String dynamicKey = keyFactory.getDynamicKey(cts.getCE(),cts.getTN());
        byte[] ret = base64.decode(src.getBytes());
        ret = codeConvertor.decode(ret);
        ret = des.decrypt(ret, dynamicKey);
        ret = codeConvertor.decode(ret);
        ret = des.decrypt(ret, fixKey);
        ret = codeConvertor.decode(ret);

        int count = 0;
        while (!iterator.isAfterLast()){
            String randomKey = iterator.next();
            try{
                byte[] tmp = des.decrypt(ret, randomKey);
                String so = new String(tmp);
                String som = SOToSOM(cts, so);
                if(som.equals(cts.getSOM())){
                    return so;
                }
            }catch (Exception e){
            } finally {
                count++;
                XLog.i("test count:"+count);
            }
        }
        return null;
    }




    private String decodeLevelMedium(CTS cts, String src) throws Exception {
        CodeConvertor codeConvertor = new ShiftCodeConvertor(cts.getSOM().getBytes()[10]);
        String fixKey = keyFactory.getFixedKey();
        String dynamicKey = keyFactory.getDynamicKey(cts.getCE(),cts.getTN(),cts.getSOM());
        byte[] ret = base64.decode(src.getBytes());
        ret = des.decrypt(ret, fixKey);
        ret = codeConvertor.decode(ret);
        ret = des.decrypt(ret, dynamicKey);
        return new String(ret);
    }




    private String decodeLevelLow(CTS cts,String src) throws Exception {
        CodeConvertor codeConvertor = new ShiftCodeConvertor(cts.getSOM().charAt(cts.getSOM().length()-2));
        String key = keyFactory.getFixedKey();
        byte[] ret = base64.decode(src.getBytes());
        ret = codeConvertor.decode(ret);
        ret = des.decrypt(ret, key);
        return new String(ret);
    }
}

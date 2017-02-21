package com.forthe.xmemory.frame;

import com.forthe.xlog.XLog;
import com.forthe.xmemory.core.ByteStr;
import com.forthe.xmemory.core.CodeConvertor;
import com.forthe.xmemory.core.Const;
import com.forthe.xmemory.core.Device;
import com.forthe.xmemory.core.KeyFactory;
import com.forthe.xmemory.core.MD5;
import com.forthe.xmemory.core.Mix;
import com.forthe.xmemory.core.Reversal;
import com.forthe.xmemory.core.YETPool;


public class KeyFactoryImpl implements KeyFactory {
    private Device device;
    public KeyFactoryImpl() {
        device = new DeviceImpl();
    }

    @Override
    public String getFixedKey() {
        return device.getKey();
    }

    @Override
    public String getDynamicKey(String... s) {
        if(null == s || s.length == 0){
            throw new IllegalArgumentException("argument can not none");
        }
        Reversal rever = new ReversalImpl();
        String[] tmps = new String[s.length];
        MD5 md5 = new MD5Impl();
        for(int i=0;i<s.length;i++){
            tmps[i] = md5.md5(rever.reversal(s[i]));
        }
        Mix mix = new MixImpl();
        String ret = mix.mix(tmps);
        ret = rever.reversal(ret);
        return ret.substring(0, 8);
    }

    @Override
    public String getRestrictedRandomKey() {
        String[] yetArray = new String[3];
        ByteStr byteStr = new HexByteStr();
        CodeConvertor codeConvertor = new ShiftCodeConvertor(4);
        YETPool yetPool = new YETPoolImpl();
        String[][] yet = yetPool.queryColumns();
        Mix mix = new MixImpl();

        int index = (int) (Math.random()* Const.YET_POOLSIZE);
        String  yetStr = yet[index][0];
        byte[] tmp = byteStr.strToByte(yetStr);
        tmp = codeConvertor.decode(tmp);
        yetArray[0] = new String(tmp).substring(0, 3);


        index = (int) (Math.random()* Const.YET_POOLSIZE);
        yetStr = yet[index][1];
        tmp = byteStr.strToByte(yetStr);
        tmp = codeConvertor.decode(tmp);
        yetArray[1] = new String(tmp).substring(3, 5);


        index = (int) (Math.random()* Const.YET_POOLSIZE);
        yetStr = yet[index][2];
        tmp = byteStr.strToByte(yetStr);
        tmp = codeConvertor.decode(tmp);
        yetArray[2] = new String(tmp).substring(5, 8);
        return mix.mix(yetArray[1], yetArray[2], yetArray[0]);
    }
}

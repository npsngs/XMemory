package com.forthe.xmemory.frame;

import com.forthe.xmemory.core.ByteStr;
import com.forthe.xmemory.core.CodeConvertor;
import com.forthe.xmemory.core.Const;
import com.forthe.xmemory.core.Iterator;
import com.forthe.xmemory.core.Mix;


public class YETIterator implements Iterator<String> {
    private String[][] yet;
    private int indexI,indexJ,indexK;
    private Mix mix;
    public YETIterator() {
        mix = new MixImpl();
        yet = new YETPoolImpl().queryColumns();
        indexI = 0;
        indexJ = 0;
        indexK = 0;
    }

    @Override
    public boolean isAfterLast() {
        return indexI >= Const.YET_POOLSIZE;
    }

    @Override
    public String next() {
        String[] yetArray = new String[3];
        ByteStr byteStr = new HexByteStr();
        CodeConvertor codeConvertor = new ShiftCodeConvertor(4);

        String  yetStr = yet[indexI][0];
        byte[] tmp = byteStr.strToByte(yetStr);
        tmp = codeConvertor.decode(tmp);
        yetArray[0] = new String(tmp).substring(0, 3);


        yetStr = yet[indexJ][1];
        tmp = byteStr.strToByte(yetStr);
        tmp = codeConvertor.decode(tmp);
        yetArray[1] = new String(tmp).substring(3, 5);

        yetStr = yet[indexK][2];
        tmp = byteStr.strToByte(yetStr);
        tmp = codeConvertor.decode(tmp);
        yetArray[2] = new String(tmp).substring(5, 8);

        indexK++;
        if(indexK >= Const.YET_POOLSIZE){
            indexK = 0;
            indexJ++;
            if(indexJ >= Const.YET_POOLSIZE){
                indexJ = 0;
                indexI++;
            }
        }

        return mix.mix(yetArray[1], yetArray[2], yetArray[0]);
    }
}

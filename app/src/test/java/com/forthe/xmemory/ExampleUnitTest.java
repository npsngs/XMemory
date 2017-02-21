package com.forthe.xmemory;


import com.forthe.xmemory.frame.ShiftCodeConvertor;

import org.junit.Test;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testShiftCodeConvertor() throws Exception {
        ShiftCodeConvertor codeConvertor = new ShiftCodeConvertor(-12);
        System.out.println("shift:"+codeConvertor.getShift());
        byte src = (byte) 63;
        System.out.println("src:"+Integer.toBinaryString(src));

        byte ret = codeConvertor.shiftByte(src, 3);

        System.out.println("ret:"+Integer.toBinaryString(ret));

        src = codeConvertor.shiftByte(ret, 5);

        System.out.println("src:"+Integer.toBinaryString(src));

        String s = "喔";
        System.out.println("----------------------");
        System.out.println("s:"+s);
        System.out.println("src:"+EncodeUtils.byteToHexString(s.getBytes()));
        byte[] rets = codeConvertor.encode(s.getBytes());
        String r = EncodeUtils.byteToHexString(rets);
        System.out.println("ret:"+r);
        rets = EncodeUtils.hexStringToByte(r);
        byte[] srcs = codeConvertor.decode(rets);
        System.out.println("src:"+EncodeUtils.byteToHexString(srcs));
        System.out.println("s:"+new String(srcs));
    }
}
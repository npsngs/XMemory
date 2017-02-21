package com.forthe.xmemory.frame;

import com.forthe.xmemory.core.CodeConvertor;


public class ShiftCodeConvertor implements CodeConvertor {
    private final int shift;
    public ShiftCodeConvertor(int shift) {
        this.shift = Math.abs(shift)%4+3;
    }

    public int getShift() {
        return shift;
    }

    @Override
    public byte[] encode(byte[] src) {
        int srcPos, targetPos, tmpShift;
        byte[] target = new byte[src.length];
        for (int i=0;i<src.length;){
            srcPos = i+shift-1;
            targetPos = i;
            tmpShift = shift;
            while (tmpShift > 0){
                if(srcPos < src.length){
                    target[targetPos] = shiftByte(src[srcPos], shift);
                    targetPos++;
                }
                tmpShift--;
                srcPos--;
            }
            i += shift;
        }
        return target;
    }



    @Override
    public byte[] decode(byte[] src) {
        int srcPos, targetPos, tmpShift;
        byte[] target = new byte[src.length];
        for (int i=0;i<src.length;){
            srcPos = i+shift-1;
            targetPos = i;
            tmpShift = shift;
            while (tmpShift > 0){
                if(srcPos < src.length){
                    target[targetPos] = shiftByte(src[srcPos], 8-shift);
                    targetPos++;
                }
                tmpShift--;
                srcPos--;
            }
            i += shift;
        }
        return target;
    }

    public byte shiftByte(byte src, int shift){
        int localSrc = src;
        int localShift = shift;
        int mask = 128;
        while (localShift > 0){
            int r = localSrc&mask;
            if(0 == r){
                localSrc = localSrc<<1;
            }else{
                localSrc = localSrc<<1;
                localSrc += 1;
            }
            localShift--;
        }
        return (byte) (localSrc&0xff);
    }
}

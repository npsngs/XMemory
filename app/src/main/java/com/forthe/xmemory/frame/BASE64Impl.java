package com.forthe.xmemory.frame;
import android.util.Base64;
import com.forthe.xmemory.core.BASE64;

public class BASE64Impl implements BASE64 {
    @Override
    public byte[] encode(byte[] input) {
        return Base64.encode(input, Base64.DEFAULT);
    }

    @Override
    public byte[] decode(byte[] input) {
        return Base64.decode(input, Base64.DEFAULT);
    }
}

package com.forthe.xmemory.core;

public interface CodeConvertor {
    byte[] encode(byte[] src);
    byte[] decode(byte[] src);
}

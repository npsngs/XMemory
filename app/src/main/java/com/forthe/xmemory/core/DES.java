package com.forthe.xmemory.core;

public interface DES {
    byte[] encrypt(byte[] input, String key) throws Exception;
    byte[] decrypt(byte[] input, String key) throws Exception;

}

package com.forthe.xmemory.core;

public interface KeyFactory {
    String getFixedKey();
    String getDynamicKey(String... s);
    String getRestrictedRandomKey();
}

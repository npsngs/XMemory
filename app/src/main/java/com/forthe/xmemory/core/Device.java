package com.forthe.xmemory.core;

import android.content.Context;

public interface Device {
    String getKey();
    String createDeviceKey(Context context);
}

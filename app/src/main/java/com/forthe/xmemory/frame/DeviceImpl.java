package com.forthe.xmemory.frame;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.forthe.xmemory.core.Const;
import com.forthe.xmemory.core.Device;
import com.forthe.xmemory.core.MD5;
import com.forthe.xmemory.core.Mix;

import java.io.InputStreamReader;
import java.io.LineNumberReader;


public class DeviceImpl implements Device {
    private static String key;
    @Override
    public String getKey() {
        return key;
    }

    private String getKeyTimestamp(Context context){
        SharedPreferences sp = context.getSharedPreferences(Const.SP_KEY, Context.MODE_PRIVATE);
        return sp.getString("sod_svl", null);
    }

    /**
     * 获取设备唯一识别码
     */

    public String getUniqueId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String tmDevice = tm.getDeviceId();
            if (TextUtils.isEmpty(tmDevice)) {
                Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
                InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                tmDevice = input.readLine();
                if (tmDevice != null) {
                    tmDevice = tmDevice.trim();// 去空格
                } else {
                    // 赋予默认值
                    WifiManager manager = (WifiManager) context.getSystemService(
                            Context.WIFI_SERVICE);
                    tmDevice = manager.getConnectionInfo().getMacAddress();
                }
            }
            return tmDevice;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String createDeviceKey(Context context) {
        if(null != key){
            return key;
        }
        String timestamp = getKeyTimestamp(context);
        String idcode = getUniqueId(context);
        MD5 md5 = new MD5Impl();
        Mix mix = new MixImpl();
        if(TextUtils.isEmpty(timestamp) || TextUtils.isEmpty(idcode)){
            String m = md5.md5(Const.SP_KEY);
            m = mix.mix(m, Const.SP_KEY);
            key = md5.md5(m);
        }else{
            String m = mix.mix(idcode, timestamp, timestamp);
            m = md5.md5(m);
            m = mix.mix(idcode, m, timestamp);
            key = md5.md5(m);
        }

        if(null == key){
            throw new IllegalStateException("create dk failed");
        }
        return key;
    }
}

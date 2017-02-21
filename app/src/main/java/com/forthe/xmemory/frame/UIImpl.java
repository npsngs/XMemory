package com.forthe.xmemory.frame;

import android.content.Context;
import android.content.SharedPreferences;

import com.forthe.xlog.XLog;
import com.forthe.xmemory.core.CTS;
import com.forthe.xmemory.core.CTSStore;
import com.forthe.xmemory.core.Const;
import com.forthe.xmemory.core.Device;
import com.forthe.xmemory.core.SOConvertor;
import com.forthe.xmemory.core.YETPool;
import com.forthe.xmemory.core.UI;

import java.util.List;


public class UIImpl implements UI {
    private SOConvertor convertor;
    private CTSStore ctsStore;
    private YETPool yetPool;

    @Override
    public void init(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Const.SP_KEY, Context.MODE_PRIVATE);
        boolean hasInit = sp.getBoolean("hit",false);
        if(!hasInit){
            sp.edit().putString("sod_svl", System.currentTimeMillis()+"").putBoolean("hit", true).apply();
        }

        Device device = new DeviceImpl();
        device.createDeviceKey(context);

        DBHelper.init(context);
        ctsStore = new CTSStoreImpl();
        yetPool = new YETPoolImpl();
        convertor = new SOConvertorImpl();
        if(!yetPool.hasInited()){
            XLog.i("fill pool start");
            yetPool.fillPool();
            XLog.i("fill pool success");
        }
    }

    @Override
    public void insert(String TN, String SO, int svl) throws Exception {
        CTS cts = new CTS();
        cts.setCE(convertor.TNToCE(TN));
        cts.setTN(TN);
        cts.setSOM(convertor.SOToSOM(cts, SO));
        cts.setSVL(svl);
        convertor.encode(cts, SO);
        ctsStore.insert(cts);
    }

    @Override
    public void insert(String TN, String SO, int svl, String TA) throws Exception {
        CTS cts = new CTS();
        cts.setTA(TA);
        cts.setCE(convertor.TNToCE(TN));
        cts.setTN(TN);
        cts.setSOM(convertor.SOToSOM(cts, SO));
        cts.setSVL(svl);
        convertor.encode(cts, SO);
        ctsStore.insert(cts);
    }

    @Override
    public String querySO(String TN) throws Exception {
        CTS cts;
        List<CTS> ctss = ctsStore.queryByTN(TN);
        if(null != ctss && ctss.size() > 0){
            cts = ctss.get(0);
        }else{
            return null;
        }
        return convertor.decode(cts);
    }

    @Override
    public boolean testSO(String TN, String SO) throws Exception {
        CTS tmp = new CTS();
        tmp.setCE(convertor.TNToCE(TN));
        tmp.setTN(TN);
        String som = convertor.SOToSOM(tmp,SO);

        List<CTS> ctss = ctsStore.queryByTN(TN);
        if(null != ctss && ctss.size() > 0){
            for (CTS cts:ctss){
                if(som.equals(cts.getSOM())){
                    return true;
                }
            }
        }
        return false;
    }
}

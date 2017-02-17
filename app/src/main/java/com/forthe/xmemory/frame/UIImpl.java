package com.forthe.xmemory.frame;

import android.content.Context;
import android.text.TextUtils;

import com.forthe.xlog.XLog;
import com.forthe.xmemory.core.CTS;
import com.forthe.xmemory.core.CTSStore;
import com.forthe.xmemory.core.Const;
import com.forthe.xmemory.core.SODPool;
import com.forthe.xmemory.core.SOTransverter;
import com.forthe.xmemory.core.UI;

import java.util.List;


public class UIImpl implements UI {
    private SOTransverter transverter;
    private CTSStore ctsStore;
    private SODPool sodPool;
    public UIImpl(Context context) {
        transverter = new SOTransverterImpl();
        ctsStore = new CTSStoreImpl(context);
        sodPool = new SODPoolImpl(context);
    }

    @Override
    public void insert(String CE, String TN, String SO) throws Exception {
        String key = transverter.CETN2Key(CE,TN);
        String som = transverter.SO2SOM(SO, key);
        String sod = transverter.SO2SOD(SO, key);
        CTS cts = new CTS();
        cts.setCE(CE);
        cts.setTN(TN);
        cts.setSOM(som);
        ctsStore.insert(cts);
        sodPool.insert(sod);
    }

    @Override
    public String querySO(String CE, String TN) throws Exception {
        String som = null;
        List<CTS> ctss = ctsStore.queryByCETN(CE, TN);
        if(null != ctss && ctss.size() > 0){
            som = ctss.get(0).getSOM();
        }

        if(TextUtils.isEmpty(som)){
            return null;
        }

        String[] tmpSos = new String[3];
        String key = transverter.CETN2Key(CE, TN);
        String[][] sos = sodPool.queryColumns();

        int count;
        for(int i=0;i< Const.SOD_POOLSIZE;i++){
            for(int j=0;j< Const.SOD_POOLSIZE;j++){
                for(int k=0;k< Const.SOD_POOLSIZE;k++){
                    tmpSos[0] = sos[i][0];
                    tmpSos[1] = sos[j][1];
                    tmpSos[2] = sos[k][2];

                    String sod = transverter.SOS2SOD(tmpSos);
                    count = i*Const.SOD_POOLSIZE*Const.SOD_POOLSIZE +j*Const.SOD_POOLSIZE + k +1;
                    XLog.d("sod", "count:"+count);
                    String tmpSom = transverter.SOD2SOM(sod, key);
                    if(som.equals(tmpSom)){
                        return transverter.SOD2SO(sod,key);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean testSO(String CE, String TN, String SO) throws Exception {
        String key = transverter.CETN2Key(CE,TN);
        String som = transverter.SO2SOM(SO, key);
        List<CTS> ctss = ctsStore.queryByCETN(CE, TN);
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

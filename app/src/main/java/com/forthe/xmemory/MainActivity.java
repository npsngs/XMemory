package com.forthe.xmemory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.forthe.xlog.XLog;
import com.forthe.xmemory.core.BASE64;
import com.forthe.xmemory.core.DES;
import com.forthe.xmemory.core.Reversal;
import com.forthe.xmemory.core.SODPool;
import com.forthe.xmemory.core.SOTransverter;
import com.forthe.xmemory.core.UI;
import com.forthe.xmemory.frame.BASE64Impl;
import com.forthe.xmemory.frame.DESImpl;
import com.forthe.xmemory.frame.ReversalImpl;
import com.forthe.xmemory.frame.SODPoolImpl;
import com.forthe.xmemory.frame.SOTransverterImpl;
import com.forthe.xmemory.frame.UIImpl;

public class MainActivity extends Activity implements View.OnClickListener{
    private EditText et_tag,et_name, et_value;
    private UI ui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XLog.init(getApplication(), getSaveDir());

        et_tag = (EditText) findViewById(R.id.et_tag);
        et_name = (EditText) findViewById(R.id.et_name);
        et_value = (EditText) findViewById(R.id.et_value);

        findViewById(R.id.bt_insert).setOnClickListener(this);
        findViewById(R.id.bt_query).setOnClickListener(this);
        findViewById(R.id.bt_test).setOnClickListener(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                testDES();
            }
        });
        new Thread(){
            @Override
            public void run() {

                SODPool sodPool = new SODPoolImpl(getApplicationContext());
                if(!sodPool.hasInited()){
                    XLog.i("fill pool start");
                    sodPool.fillPool();
                    XLog.i("fill pool success");
                }

            }
        }.start();

        ui= new UIImpl(getApplicationContext());
    }



    private void testDES(){
        DES des = new DESImpl();
        BASE64 base64 = new BASE64Impl();
        SOTransverter transverter = new SOTransverterImpl();
        try {
            byte[] ret = des.encrypt("ssssssss".getBytes(),"11111111");
            ret = base64.encode(ret);
            String[] ss = transverter.SOD2SOS(new String(ret));
            XLog.d("ret1:"+ss[0]);
            XLog.d("ret2:"+ss[1]);
            XLog.d("ret3:"+ss[2]);

            String s = transverter.SOS2SOD(ss);
            XLog.d("s:"+s);

            ret = base64.decode(s.getBytes());
            ret = des.decrypt(ret,"11111111");
            XLog.d("source:"+new String(ret));
        } catch (Exception e) {
            e.printStackTrace();
            XLog.e(e.getMessage());
        }
    }


    private String getSaveDir(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/XMemory";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode ==  KeyEvent.KEYCODE_MENU){
            XLog.showLog(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_insert: {
                String CE = et_tag.getText().toString();
                String TN = et_name.getText().toString();
                String SO = et_value.getText().toString();
                if (TextUtils.isEmpty(CE) || TextUtils.isEmpty(TN) || TextUtils.isEmpty(SO)) {
                    return;
                }

                try {
                    ui.insert(CE, TN, SO);
                } catch (Exception e) {
                    e.printStackTrace();
                    XLog.w(e.getMessage());
                }

                break;
            }case R.id.bt_query: {
                final String CE = et_tag.getText().toString();
                final String TN = et_name.getText().toString();
                if (TextUtils.isEmpty(CE) || TextUtils.isEmpty(TN)) {
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            XLog.d("querySo start");
                            final String so = ui.querySO(CE, TN);

                            XLog.d("querySo ended so"+so);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    et_value.setText(null==so?"":so);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }break;
            case R.id.bt_test: {
                String CE = et_tag.getText().toString();
                String TN = et_name.getText().toString();
                String SO = et_value.getText().toString();
                if (TextUtils.isEmpty(CE) || TextUtils.isEmpty(TN) || TextUtils.isEmpty(SO)) {
                    return;
                }

                try {
                    boolean isRight = ui.testSO(CE, TN, SO);
                    Toast.makeText(this,""+isRight,Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    XLog.w(e.getMessage());
                }
            }break;
        }
    }
}

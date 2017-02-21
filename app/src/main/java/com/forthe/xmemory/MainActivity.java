package com.forthe.xmemory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.forthe.xlog.XLog;
import com.forthe.xmemory.core.CTS;
import com.forthe.xmemory.core.Const;
import com.forthe.xmemory.core.SOConvertor;
import com.forthe.xmemory.core.UI;
import com.forthe.xmemory.frame.SOConvertorImpl;
import com.forthe.xmemory.frame.UIImpl;
import com.forthe.xmemory.widget.InputPart;

public class MainActivity extends Activity implements View.OnClickListener{
    private TextView btn_save;
    private TextView btn_query;
    private InputPart inputPart;
    private Spinner spinner;
    private UI ui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_main);
        XLog.init(getApplication(), getSaveDir());
        spinner = (Spinner) findViewById(R.id.spinner);
        btn_save = (TextView) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_save.setEnabled(false);
        btn_query = (TextView) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);
        btn_query.setEnabled(false);
        inputPart = new InputPart(this) {
            @Override
            protected void onTextChange() {
                String TN = inputPart.getTitle();
                String SO = inputPart.getContent();
                if (!TextUtils.isEmpty(TN) && !TextUtils.isEmpty(SO)) {
                    btn_save.setEnabled(true);
                }else if(!TextUtils.isEmpty(TN)) {
                    btn_query.setEnabled(true);
                }else {
                    btn_save.setEnabled(false);
                    btn_query.setEnabled(false);
                }
            }
        };

        ui= new UIImpl();
        new Thread(){
            @Override
            public void run() {
                ui.init(getApplicationContext());
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        testDES();
                    }
                });*/
            }
        }.start();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] svls = getResources().getStringArray(R.array.svl);
                svl = Integer.parseInt(svls[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                svl = 0;
            }
        });
    }



    private void testDES(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    XLog.d("Start Test SOConvertor");
                    SOConvertor convertor = new SOConvertorImpl();
                    CTS cts = new CTS();
                    cts.setCE("QQ");
                    cts.setTN("3363445091");
                    cts.setSOM(convertor.SOToSOM(cts, "tom123321"));
                    cts.setSVL(Const.SVL_1);
                    convertor.encode(cts, "tom123321");

                    XLog.d("Level1 cts:"+cts.toString());
                    String so = convertor.decode(cts);
                    XLog.d("Level1 so:"+so);

                    cts.setSVL(Const.SVL_2);
                    convertor.encode(cts, "tom123321");
                    XLog.d("Level2 cts:"+cts.toString());
                    so = convertor.decode(cts);
                    XLog.d("Level2 so:"+so);

                    cts.setSVL(Const.SVL_3);
                    convertor.encode(cts, "tom123321");
                    XLog.d("Level3 cts:"+cts.toString());
                    so = convertor.decode(cts);
                    XLog.d("Level3 so:"+so);



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    private int svl = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save: {
                String TA = inputPart.getLabel();
                String TN = inputPart.getTitle();
                String SO = inputPart.getContent();
                if (TextUtils.isEmpty(TN) || TextUtils.isEmpty(SO)) {
                    return;
                }

                try {
                    ui.insert(TN, SO, svl,TA);
                    XLog.d("save success");
                } catch (Exception e) {
                    e.printStackTrace();
                    XLog.w(e.getMessage());
                }

                break;
            }case R.id.btn_query: {
                final String TN = inputPart.getTitle();
                if (TextUtils.isEmpty(TN)) {
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            XLog.d("querySo start");
                            final String so = ui.querySO(TN);

                            XLog.d("querySo ended so"+so);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    inputPart.setContent(null==so?"":so);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }break;
        }
    }
}

package com.forthe.xmemory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class QueryActivity extends Activity {
    public void goQueryActivity(Activity from){
        Intent intent = new Intent(from, QueryActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

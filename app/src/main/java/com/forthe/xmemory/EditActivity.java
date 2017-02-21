package com.forthe.xmemory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class EditActivity extends Activity {
    private static final int MODE_EDIT =  1;
    private static final int MODE_QUERY = 2;

    public static void goEditActivity(Activity from){
        Intent intent = new Intent(from, EditActivity.class);
        intent.putExtra("mode", MODE_EDIT);
        from.startActivityForResult(intent, 1024);
    }

    public static void goEditActivity(Activity from, String TN){
        Intent intent = new Intent(from, EditActivity.class);
        intent.putExtra("mode", MODE_EDIT);
        intent.putExtra("TN",TN);
        from.startActivityForResult(intent, 1024);
    }

    public static void goQueryActivity(Activity from){
        Intent intent = new Intent(from, EditActivity.class);
        intent.putExtra("mode", MODE_QUERY);
        from.startActivityForResult(intent, 1024);
    }

    public static void goQueryActivity(Activity from, String TN){
        Intent intent = new Intent(from, EditActivity.class);
        intent.putExtra("TN",TN);
        intent.putExtra("mode", MODE_QUERY);
        from.startActivityForResult(intent, 1024);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

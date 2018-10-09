package com.lz.workmanager.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBaseBtnClick(View view) {
        Intent intent = new Intent(MainActivity.this, BasicsActivity.class);
        startActivity(intent);
    }

    public void onAdvanceBtnClick(View view) {
        Intent intent = new Intent(MainActivity.this, AdvanceActivity.class);
        startActivity(intent);
    }
}

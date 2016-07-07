package com.pumelotech.dev.mypigeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.pumelotech.dev.mypigeon.BLE.PacketParser;

public class MainActivity extends AppCompatActivity {

    public static String DebugTag = MyApplication.DebugTag;
    private PacketParser mPacketParser;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        Button BT_ManagerPigeon = (Button) findViewById(R.id.BT_managerPigeon);
        BT_ManagerPigeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PigeonListActivity.class));
            }
        });

        mPacketParser = PacketParser.getInstance(this);
        MyPigeonDAO.getInstance(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    boolean Toggle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}

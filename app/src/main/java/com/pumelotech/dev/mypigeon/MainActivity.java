package com.pumelotech.dev.mypigeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.pumelotech.dev.mypigeon.BLE.PacketParser;

public class MainActivity extends AppCompatActivity {

    public static String DebugTag = MyApplication.DebugTag;
    private PacketParser mPacketParser;
    MyApplication myApplication;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_main_page);
        mToolbar.setTitle("我的鸽子");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(mToolbar);
        Button BT_ManagerPigeon = (Button) findViewById(R.id.BT_managerPigeon);
        BT_ManagerPigeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PigeonListActivity.class));
            }
        });
        imageButton = (ImageButton) findViewById(R.id.bluetooth_state);
        imageButton.setEnabled(false);
        mPacketParser = PacketParser.getInstance(this);
        mPacketParser.registerCallback(callBack);
        MyPigeonDAO.getInstance(this);
    }

    PacketParser.CallBack callBack = new PacketParser.CallBack() {
        @Override
        public void onSendSuccess() {

        }

        @Override
        public void onSendFailure() {

        }

        @Override
        public void onTimeOut() {

        }

        @Override
        public void onConnectStatusChanged(boolean status) {
            final boolean s = status;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageButton.setEnabled(s);
                }
            });

        }

        @Override
        public void onDataReceived(byte category) {

        }

        @Override
        public void onCharacteristicNotFound() {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    boolean Toggle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}

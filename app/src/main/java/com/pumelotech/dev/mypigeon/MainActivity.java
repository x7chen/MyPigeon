package com.pumelotech.dev.mypigeon;

import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pumelotech.dev.mypigeon.BLE.ConnectionCallback;
import com.pumelotech.dev.mypigeon.BLE.LeConnector;
import com.pumelotech.dev.mypigeon.BLE.PacketParser;
import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MyApplication.DebugTag;
    private PacketParser mPacketParser;
    ImageButton imageButton;
    LeConnector mLeConnector;
    MyPigeonDAO myPigeonDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_main_page);
        mToolbar.setTitle("我的鸽子");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(mToolbar);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.pigeon_overview);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managerPigeon();
            }
        });
        imageButton = (ImageButton) findViewById(R.id.bluetooth_state);

        mLeConnector = LeConnector.getInstance(this);
        MyApplication.mLeConnector = mLeConnector;
        if (mLeConnector.getmConnectionState() == LeConnector.STATE_CONNECTED) {
            imageButton.setEnabled(true);
        } else {
            imageButton.setEnabled(false);
        }
        mLeConnector.autoConnect("BT05", callBack);
        myPigeonDAO = MyPigeonDAO.getInstance(this);


        mPacketParser = PacketParser.getInstance(this);
        polling();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView flyCount = (TextView) findViewById(R.id.main_fly_count);
        TextView restCount = (TextView) findViewById(R.id.main_rest_count);
        int fly = 0;
        int rest = 0;
        List<PigeonInfo> pigeonInfoList = myPigeonDAO.getAllPigeon();
        for (PigeonInfo pigeon : pigeonInfoList) {
            if (pigeon.Status.equals("FLY")) {
                fly++;
            } else {
                rest++;
            }
        }
        flyCount.setText(String.valueOf(fly));
        restCount.setText(String.valueOf(rest));
    }
    void managerPigeon(){
        startActivity(new Intent(MainActivity.this, PigeonListActivity.class));
    }
    ConnectionCallback callBack = new ConnectionCallback() {
        @Override
        public void onConnectionStateChange(int newState) {
            final boolean status;
            status = newState == BluetoothGatt.STATE_CONNECTED;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageButton.setEnabled(status);
                }
            });
        }

        @Override
        public void onError(String message, int errorCode) {

        }

        @Override
        public void onDeviceNotSupported() {

        }
    };

    public void polling() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                mPacketParser.requestRecord();
            }
        }, 1000, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}

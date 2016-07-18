package com.pumelotech.dev.mypigeon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.pumelotech.dev.mypigeon.Adapter.RecordListAdapter;
import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;
import com.pumelotech.dev.mypigeon.DataType.RecordInfo;

import java.util.List;

public class RecordActivity extends AppCompatActivity {
    private final static String TAG = MyApplication.DebugTag;
    List<RecordInfo> recordInfoList;
    RecordListAdapter mRecordListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        StatusBarCompat.compat(this,0xFF000000);

        String name = getIntent().getStringExtra("Name");
        String ID = getIntent().getStringExtra("ID");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_record);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setTitle(name);
        mToolbar.setSubtitle("飞行记录");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ListView listView = (ListView) findViewById(R.id.listView_record);
        mRecordListAdapter= new RecordListAdapter(this);
        MyApplication.recordActivity = this;
        MyApplication.mRecordListAdapter = mRecordListAdapter;
        listView.setAdapter(mRecordListAdapter);
        MyPigeonDAO  myPigeonDAO= MyPigeonDAO.getInstance();
        recordInfoList = myPigeonDAO.getPigeonRecord(ID);
        for(RecordInfo record:recordInfoList) {
            mRecordListAdapter.addRecord(record);
        }

        mRecordListAdapter.notifyDataSetChanged();
    }

    public void updateRecord(RecordInfo record) {
        mRecordListAdapter.setRecord(record);
        if (mRecordListAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecordListAdapter.notifyDataSetChanged();
                }
            });

        }
    }
}

package com.pumelotech.dev.mypigeon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.pumelotech.dev.mypigeon.Adapter.RecordListAdapter;
import com.pumelotech.dev.mypigeon.DataType.RecordInfo;

public class RecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        String name = getIntent().getStringExtra("Name");
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
        final RecordListAdapter mRecordListAdapter= new RecordListAdapter(this);
        listView.setAdapter(mRecordListAdapter);
        for(int i=0;i<10;i++) {
            RecordInfo recordInfo = new RecordInfo();
            mRecordListAdapter.addRecord(recordInfo);
        }

        mRecordListAdapter.notifyDataSetChanged();
    }
}

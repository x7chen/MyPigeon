package com.pumelotech.dev.mypigeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.pumelotech.dev.mypigeon.Adapter.PigeonListAdapter;
import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;

import java.util.List;


public class PigeonListActivity extends AppCompatActivity {

    PigeonListAdapter mPigeonListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pigeon_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button BT_addPigeon = (Button) findViewById(R.id.BT_addPigeon);
        BT_addPigeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PigeonListActivity.this,PigeonEditActivity.class));
            }
        });
        ListView listView = (ListView) findViewById(R.id.listView_pigeon);
        mPigeonListAdapter = new PigeonListAdapter(this);

        MyApplication.savePigeonListAdapter(mPigeonListAdapter);
        listView.setAdapter(mPigeonListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PigeonInfo pigeon = mPigeonListAdapter.getPigeon(position);
                if (pigeon == null) return;
                Intent intent = new Intent(PigeonListActivity.this, RecordActivity.class);
                intent.putExtra("Name", pigeon.Name);
                startActivity(intent);
            }
        });
        MyPigeonDAO myPigeonDAO = MyPigeonDAO.getInstance();
        List<PigeonInfo> allPigeon = null;
        if (myPigeonDAO != null) {
            allPigeon = myPigeonDAO.getAllPigeon();
        }
        if (allPigeon != null) {
            for (PigeonInfo pigeon : allPigeon) {
                mPigeonListAdapter.addPigeon(pigeon);
            }
        }
        mPigeonListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fly, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_fly){
            if(item.getTitle().toString().equals("开始放飞")) {
                for (int i = 0; i < mPigeonListAdapter.getCount(); i++) {
                    mPigeonListAdapter.getPigeon(i).FlyEnable = "Enable";
                }
                mPigeonListAdapter.notifyDataSetChanged();
                item.setTitle("结束放飞");
            }else {
                for (int i = 0; i < mPigeonListAdapter.getCount(); i++) {
                    mPigeonListAdapter.getPigeon(i).FlyEnable = "Disable";
                }
                mPigeonListAdapter.notifyDataSetChanged();
                item.setTitle("开始放飞");
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.pumelotech.dev.mypigeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.pumelotech.dev.mypigeon.Adapter.HidingScrollListener;
import com.pumelotech.dev.mypigeon.Adapter.PigeonRecyclerAdapter;
import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;

import java.util.List;


public class PigeonListActivity extends AppCompatActivity {

    PigeonRecyclerAdapter mPigeonRecyclerAdapter;
    Toolbar mToolbar;
    ImageButton mFabButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pigeon_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_pigeon_list);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setTitle("我的鸽子");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        //setSupportActionBar 用于设置menu选项
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MyApplication.pigeonListActivity = this;
        mFabButton = (ImageButton) findViewById(R.id.BT_addPigeon);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PigeonListActivity.this, PigeonEditActivity.class));
            }
        });
        MyPigeonDAO myPigeonDAO = MyPigeonDAO.getInstance();
        List<PigeonInfo> allPigeon = null;
        if (myPigeonDAO != null) {
            allPigeon = myPigeonDAO.getAllPigeon();
        }
        MyApplication.mPigeonList = allPigeon;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_pigeon);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPigeonRecyclerAdapter= new PigeonRecyclerAdapter(allPigeon);
        MyApplication.pigeonRecyclerAdapter = mPigeonRecyclerAdapter;
        recyclerView.setAdapter(mPigeonRecyclerAdapter);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }
            @Override
            public void onShow() {
                showViews();
            }
        });
    }
    private void hideViews() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fly, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_fly) {
            if (item.getTitle().toString().equals("开始放飞")) {
                for (PigeonInfo pigeon : MyApplication.mPigeonList) {
                    pigeon.FlyEnable = "Enable";
                }
                mPigeonRecyclerAdapter.notifyDataSetChanged();
                item.setTitle("结束放飞");
            } else {
                for (PigeonInfo pigeon : MyApplication.mPigeonList) {
                    pigeon.FlyEnable = "Disable";
                }
                mPigeonRecyclerAdapter.notifyDataSetChanged();
                item.setTitle("开始放飞");
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

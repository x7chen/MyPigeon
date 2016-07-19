package com.pumelotech.dev.mypigeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    public static String TAG = MyApplication.DebugTag;
    PigeonRecyclerAdapter mPigeonRecyclerAdapter;
    List<PigeonInfo> allPigeon = null;
    Toolbar mToolbar;
    ImageButton mFabButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pigeon_list);
        StatusBarCompat.compat(this, 0xFF000000);

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

        if (myPigeonDAO != null) {
            allPigeon = myPigeonDAO.getAllPigeon();
        }
        MyApplication.mPigeonList = allPigeon;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_pigeon);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyApplication.PigeonListItemClickable = true;
        mPigeonRecyclerAdapter = new PigeonRecyclerAdapter(allPigeon);
        MyApplication.pigeonRecyclerAdapter = mPigeonRecyclerAdapter;
        recyclerView.setAdapter(mPigeonRecyclerAdapter);
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
        Log.i(TAG, "PigeonListActivity.onCreate()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "PigeonListActivity.onResume()");
    }

    private void hideViews() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
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
                MyApplication.PigeonListItemClickable = false;
                MyApplication.FlyEnable = true;
                item.setTitle("结束放飞");
            } else {
                MyApplication.PigeonListItemClickable = true;
                MyApplication.FlyEnable = false;
                item.setTitle("开始放飞");
            }
            mPigeonRecyclerAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updatePigeon(PigeonInfo pigeon) {
        for (PigeonInfo pigeonInfo : allPigeon) {
            if (pigeon.ID.equals(pigeonInfo.ID)) {
                int pigeon_index = allPigeon.indexOf(pigeonInfo);
                allPigeon.set(pigeon_index, pigeon);
                Log.i(TAG, "indexOf(pigeon):" + pigeon_index);
            }
        }
        if (mPigeonRecyclerAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPigeonRecyclerAdapter.notifyDataSetChanged();
                }
            });

        }
    }

    public void addPigeon(PigeonInfo pigeon) {
        allPigeon.add(pigeon);
        if (mPigeonRecyclerAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPigeonRecyclerAdapter.notifyDataSetChanged();
                }
            });

        }
    }
}

package com.pumelotech.dev.mypigeon;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.pumelotech.dev.mypigeon.Adapter.PigeonRecyclerAdapter;
import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sean on 2015/11/17.
 */
public class MyApplication extends Application {
    public static final String DebugTag = "MyPigeon";
    public static Typeface fontMSYH;
    public static PigeonRecyclerAdapter pigeonRecyclerAdapter;
    public static Context context;
    public static PigeonListActivity pigeonListActivity;
    public static List<PigeonInfo> mPigeonList;
    @Override
    public void onCreate() {
        super.onCreate();
        fontMSYH = Typeface.createFromAsset(getAssets(), "fonts/msyh.ttf");
        context = this;
    }
    public static void savePigeonListAdapter(PigeonRecyclerAdapter adapter){
        pigeonRecyclerAdapter = adapter;
    }
    public static PigeonRecyclerAdapter getPigeonRecyclerAdapter(){
        return pigeonRecyclerAdapter;
    }
}

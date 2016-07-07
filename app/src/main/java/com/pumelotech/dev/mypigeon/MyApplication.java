package com.pumelotech.dev.mypigeon;

import android.app.Application;
import android.graphics.Typeface;
import android.widget.Adapter;

import com.pumelotech.dev.mypigeon.Adapter.PigeonListAdapter;

/**
 * Created by Sean on 2015/11/17.
 */
public class MyApplication extends Application {

    public static Typeface fontMSYH;
    public static PigeonListAdapter pigeonListAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        fontMSYH = Typeface.createFromAsset(getAssets(), "fonts/msyh.ttf");
    }
    public static void savePigeonListAdapter(PigeonListAdapter adapter){
        pigeonListAdapter = adapter;
    }
    public static PigeonListAdapter getPigeonListAdapter(){
        return pigeonListAdapter;
    }
}

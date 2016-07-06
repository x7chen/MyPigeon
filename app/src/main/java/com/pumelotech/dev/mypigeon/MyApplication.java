package com.pumelotech.dev.mypigeon;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Created by Sean on 2015/11/17.
 */
public class MyApplication extends Application {

    public static Typeface fontMSYH;

    @Override
    public void onCreate() {
        super.onCreate();
        fontMSYH = Typeface.createFromAsset(getAssets(), "fonts/msyh.ttf");
    }
}

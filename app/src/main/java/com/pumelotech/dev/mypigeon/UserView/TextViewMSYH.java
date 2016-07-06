package com.pumelotech.dev.mypigeon.UserView;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pumelotech.dev.mypigeon.MyApplication;

/**
 * Created by Administrator on 2016/7/6.
 */
public class TextViewMSYH extends TextView{

    public TextViewMSYH(Context context) {
        super(context);
        init(context);
    }

    public TextViewMSYH(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewMSYH(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        this.setTypeface(MyApplication.fontMSYH);
        this.getPaint().setFakeBoldText(true);
    }
}

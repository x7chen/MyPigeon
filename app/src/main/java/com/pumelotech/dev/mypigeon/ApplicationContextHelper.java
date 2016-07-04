package com.pumelotech.dev.mypigeon;

import android.app.Application;

/**
 * Created by Sean on 2015/11/17.
 */
public class ApplicationContextHelper extends Application {

    MainActivity mainActivity;



    LeConnector leConnector;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public LeConnector getLeConnector() {
        return leConnector;
    }

    public void setLeConnector(LeConnector leConnector) {
        this.leConnector = leConnector;
    }
}

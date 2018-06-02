package com.qualityengineer.dearc.qe;

import android.app.Application;

public class IntroSlider extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Globals.init(this);
    }
}
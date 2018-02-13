package com.xiangzi.screenelf;

import android.app.Application;

/**
 * MyApp
 *
 * @author Frank
 *         2018/2/13
 *         15866818643@163.com
 */

public class MyApp extends Application {
    private static MyApp INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static MyApp getInstance() {
        return INSTANCE;
    }
}

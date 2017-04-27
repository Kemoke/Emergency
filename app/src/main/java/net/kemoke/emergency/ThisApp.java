package net.kemoke.emergency;

import android.app.Application;

import net.kemoke.emergency.api.HttpApi;

/**
 * Created by Kemoke on 27.04.2017..
 */

public class ThisApp extends Application {
    private static ThisApp instance;

    public static ThisApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        HttpApi.init();
    }
}

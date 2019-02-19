package com.one.pos;

import android.app.Application;
import android.content.Context;

import com.one.pos.service.gprint.GpPrint;
import com.one.pos.service.sunmi.Sunmi;

/**
 * @author zhumg
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Context c = getApplicationContext();

        Sunmi.init(c);
        GpPrint.init(c);
        Cache.init(c);
    }

}

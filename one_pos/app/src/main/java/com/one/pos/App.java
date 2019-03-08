package com.one.pos;

import android.content.Context;

import com.anlib.GApplication;
import com.anlib.http.Http;
import com.one.pos.db.ItemCache;
import com.one.pos.service.sunmi.Sunmi;

/**
 * @author zhumg
 */
public class App extends GApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Context c = getApplicationContext();

        //初始化缓存
        ItemCache.init(this);

        //初始化商米打印
        Sunmi.init(c);

        //http初始化
        Http.init(this);
    }

}

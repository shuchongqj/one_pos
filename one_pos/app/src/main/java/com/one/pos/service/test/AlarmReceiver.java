package com.one.pos.service.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.anlib.http.Http;
import com.anlib.http.HttpCallback;
import com.anlib.util.LogUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author zhumg
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String INTENT_ALARM_LOG = "intent_alarm_log";

    public static final String merchantGroupOrderSize = "https://p.food4u.cc/market/saas/order/openapi/merchantGroupOrderSize.do";

    private boolean httpHandler = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == INTENT_ALARM_LOG) {
            if (!httpHandler) {
                httpHandler = true;
                Log.d("AlarmReceiver", "log log log");
                Map<String, String> params = new HashMap<>();
                params.put("passportId", "100000");
                params.put("marketId", "100000");
                Http.post(context, params, merchantGroupOrderSize, new HttpCallback<OrderSize>() {
                    @Override
                    public void onSuccess(OrderSize data) {
                        //广播出去
                        LogUtils.info("http 处理数据 成功");
                        httpHandler = false;
                    }
                });
            }
        }
    }
}


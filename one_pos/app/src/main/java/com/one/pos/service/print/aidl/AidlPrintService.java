package com.one.pos.service.print.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author zhumg
 */
public abstract class AidlPrintService<T> {

    // 上下文
    protected Context context;
    // 打印線程
    protected Thread thread = null;
    // 起動標記
    protected boolean runing;

    // 等待中列表
    protected LinkedBlockingQueue<T> waitTasks = new LinkedBlockingQueue<T>();

    protected List<T> waitJoinTasks = new ArrayList<>();

    //服务
    protected IInterface aidlService;
    //连接服务
    protected ServiceConnection conn = new ServiceConnection() {
        // 當服務斷開
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("axa", "服務斷開");
            close();
        }

        // 服務連接上
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            aidlService = createService(iBinder);
            if (aidlService == null) {
                throw new RuntimeException("创建服务失败");
            }
            //启动成功，如果列表有内容，则马上加入
            if (waitJoinTasks.size() > 0) {
                for (int i = 0; i < waitJoinTasks.size(); i++) {
                    try {
                        waitTasks.offer(waitJoinTasks.get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                waitJoinTasks.clear();
            }
            if (!runing || thread == null) {
                runing = true;
                thread = new Thread(runRunnable);
                thread.start();
            }
        }
    };

    //執行線程
    protected Runnable runRunnable = new Runnable() {
        @Override
        public void run() {
            while (runing) {
                T task = null;
                try {
                    Log.e("ax", "取数据");
                    task = waitTasks.take();
                } catch (Throwable ex) {
                    runing = false;
                    Log.e("ax", "退出线程");
                    return;
                }
                if (task == null) {
                    Log.e("ax", "取数据，task为空");
                    continue;
                }
                //通知打印
                callPrint(task);
            }
        }
    };

    public AidlPrintService(Context context) {
        this.context = context;
    }

    //调用打印
    public void print(T t) {
        //如果已断开服务
        if (aidlService == null) {
            Log.e("axa", "服務为null waitJoinTasks.add");
            //重新启动
            waitJoinTasks.add(t);
            //初始化
            init();
        } else {
            Log.e("axa", "服務ok waitTasks.add");
            //直接加入，唤醒
            waitTasks.offer(t);
        }
    }

    //关闭
    public void close() {
        if (aidlService == null) {
            return;
        }
        aidlService = null;
        //解绑服务
        try {
            context.unbindService(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        runing = false;
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        waitJoinTasks.clear();
        waitTasks.clear();
    }

    protected abstract void callPrint(T t);

    protected abstract void init();

    protected abstract IInterface createService(IBinder iBinder);
}

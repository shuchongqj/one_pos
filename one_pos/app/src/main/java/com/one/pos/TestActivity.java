package com.one.pos;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.anlib.GActivity;
import com.anlib.refresh.PtrLoader;
import com.anlib.refresh.RefreshLoaderListener;
import com.anlib.util.LogUtils;
import com.anlib.util.Utils;
import com.anlib.widget.GridViewWithHeaderAndFooter;
import com.one.pos.menubar.MenuDialog;
import com.one.pos.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.util.PtrCLog;

/**
 * @author zhumg
 */
public class TestActivity extends GActivity implements View.OnClickListener {

    List<String> items;
    TestAdapter adapter;

    PtrClassicFrameLayout ptr;
    ListView listView;
    GridViewWithHeaderAndFooter gridView;

    PtrLoader ptrLoader;

    int itemIndex = 0;

    //
    private Button bt;

    @Override
    protected int getContentViewId() {
        return R.layout.ptr_gridview;
    }

    @Override
    protected void initView(View view) {

        PtrCLog.setLogLevel(PtrCLog.LEVEL_FATAL + 1);

        ptr = view.findViewById(R.id.l_ptr);
        //listView = view.findViewById(R.id.l_list_view);
        gridView = view.findViewById(R.id.l_grid_view);
        bt = view.findViewById(R.id.bt);
        bt.setOnClickListener(this);
        items = new ArrayList<>();
        adapter = new TestAdapter(this, items);

        ptrLoader = new PtrLoader(view, ptr, new RefreshLoaderListener() {
            @Override
            public void onLoading(boolean isLoadMore) {
                if (isLoadMore) {
                    getMoreDatas();
                } else {
                    getDatas();
                }
            }
        });

        //listView.setAdapter(adapter);
        gridView.setAdapter(adapter);

        View v = View.inflate(this, R.layout.test_head, null);
        gridView.addHeaderView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //测试打印
                startActivity(new Intent(TestActivity.this, MainActivity.class));
//                List<PrintComm> comms = new ArrayList<>();
//                comms.add(new PrintComm(PrintComm.COMM_TEXT, -2, 1, "门店订单 132123123123"));
//                comms.add(new PrintComm(PrintComm.COMM_TEXT, 1, 0, "订单编号：23423423"));
//                comms.add(new PrintComm(PrintComm.COMM_TEXT, 1, 1, "订单编号：23423423"));
//                comms.add(new PrintComm(PrintComm.COMM_TEXT, -1, 0, "支付方式：1号生活扫码"));
//                comms.add(new PrintComm(PrintComm.COMM_TEXT, 1, 0, "支付方式：1号生活扫码"));
//                comms.add(new PrintComm(PrintComm.COMM_NULL_LINE, 2, 1, null));
////                comms.add(new PrintComm(PrintComm.COMM_TEXT, -2, 1, "门店订单 132123123123"));
//                comms.add(new PrintComm(PrintComm.COMM_TEXT, 1, 0, "订单时间：2018-12-12 12:12:12"));
//                comms.add(new PrintComm(PrintComm.COMM_TEXT, 1, 1, "--------------------"));
//                Sunmi.print(new PrintTask(comms));
            }
        });

        ptrLoader.showLoading();
    }

    boolean error = false;

    void getDatas() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemIndex = 0;
                if (error) {
                    adapter.clearAndRefresh(null);
                    ptrLoader.refreshComplete(false, false, true);
                    return;
                }
                List<String> datas = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    datas.add("项 " + (itemIndex++));
                }
                adapter.clearAndRefresh(datas);
                ptrLoader.refreshComplete(true, false, false);
                error = true;
            }
        }, Utils.random(100, 500));
    }

    void getMoreDatas() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.random(0, 10) > 5) {
                    ptrLoader.refreshComplete(true, false, true);
                    return;
                }
                int count = Utils.random(0, 10) > 5 ? 20 : 5;
                List<String> datas = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    datas.add("项 " + (itemIndex++));
                }
                boolean hasMore = count == 20;
                LogUtils.info("hasMore: " + hasMore + ", " + count);
                adapter.addMoreAndRefresh(datas);
                ptrLoader.refreshComplete(hasMore, false, false);
            }
        }, Utils.random(100, 500));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.bt :
                MenuDialog dialog = new MenuDialog();
                dialog.show(getSupportFragmentManager(), "menuDialog");

                break;
        }


    }
}

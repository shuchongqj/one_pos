package com.one.pos.ui.main;

import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.anlib.GActivity;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.one.pos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhumg
 */
public class MainActivity extends GActivity implements View.OnClickListener {

    ListView itemtype_list_view;
    GridView item_grid_view;
    ListView cart_list_view;

    EditText et_input_code;
    EditText et_input_free;

    TextView tv_user_name;
    TextView tv_item_count;
    TextView tv_all_price;

    TextView btn_save_order;
    TextView btn_clear;
    TextView btn_down_order;

    TextView btn_add;
    TextView btn_excel;
    TextView btn_refresh;

    SimpleMarqueeView tv_market_name;
    SimpleMarqueeView tv_message;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_menu).setOnClickListener(this);

        itemtype_list_view = view.findViewById(R.id.itemtype_list_view);
        item_grid_view = view.findViewById(R.id.item_grid_view);
        cart_list_view = view.findViewById(R.id.cart_list_view);

        et_input_code = view.findViewById(R.id.et_input_code);
        et_input_free = view.findViewById(R.id.et_input_free);

        btn_save_order = view.findViewById(R.id.btn_save_order);
        btn_save_order.setOnClickListener(this);

        btn_clear = view.findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);

        btn_down_order = view.findViewById(R.id.btn_down_order);
        btn_down_order.setOnClickListener(this);

        btn_excel = view.findViewById(R.id.btn_excel);
        btn_excel.setOnClickListener(this);

        btn_refresh = view.findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(this);

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_item_count = view.findViewById(R.id.tv_item_count);
        tv_all_price = view.findViewById(R.id.tv_all_price);

        tv_market_name = view.findViewById(R.id.tv_market_name);
        SimpleMF<String> marqueeFactory = new SimpleMF(this);
        List<String> datas = new ArrayList<String>();
        datas.add("店铺名称跑马灯");
        marqueeFactory.setData(datas);
        tv_market_name.setMarqueeFactory(marqueeFactory);
        tv_market_name.startFlipping();

        tv_message = view.findViewById(R.id.tv_message);
        SimpleMF<String> marqueeFactory2 = new SimpleMF(this);
        datas = new ArrayList<String>();
        datas.add("店铺名称跑马灯");
        marqueeFactory2.setData(datas);
        tv_message.setMarqueeFactory(marqueeFactory2);
        tv_message.startFlipping();
    }


    @Override
    public void onClick(View view) {

    }
}

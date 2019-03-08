package com.gzdb.supermarket.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.SupermarketShopView;
import com.gzdb.supermarket.event.SearchOnEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Even on 2016/6/15.
 */
public class SearchView extends LinearLayout implements View.OnClickListener {

    public EditText search_Input;
    private ImageView delete_btn;
    private Context context;
    private SupermarketShopView supermarketShopView;
    Timer timer = new Timer();// 实例化Timer类
//    private Handler handler;
//    private boolean search_bool = false;

    private boolean hibe_delete_btn = true;


    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_search, this);
        initView();
//        handler = new Handler();
    }

    public void setSupermarketShopView(SupermarketShopView supermarketShopView) {
        this.supermarketShopView = supermarketShopView;
    }

//    Runnable handlerRunnable = new Runnable() {
//        @Override
//        public void run() {
//            String s = search_Input.getText().toString();
//            if (supermarketShopView != null) {
//                supermarketShopView.pinyinSearch(s);
//            }
//            search_bool = false;
//        }
//    };

    private void initView() {
        search_Input = (EditText) findViewById(R.id.search_et_input);
        delete_btn = (ImageView) findViewById(R.id.search_delete);
        search_Input.addTextChangedListener(setSearchViewListener);
        delete_btn.setOnClickListener(this);

        search_Input.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                search_Input.setFocusable(true);
                search_Input.setFocusableInTouchMode(true);
                search_Input.requestFocus();
                return false;
            }
        });
//        search_Input.setOnKeyListener(setOnKeyListener);

        search_Input.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                timer.schedule(new TimerTask() {
                    public void run() {
                        EventBus.getDefault().post(new SearchOnEvent(hasFocus));
                        this.cancel();
                    }
                }, 1000);// 这里百毫秒
            }
        });
    }

    public TextWatcher setSearchViewListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() > 0) {
                delete_btn.setVisibility(VISIBLE);
                String code=s.toString().replace(" ","");
                try {
                    if (supermarketShopView != null) {
                        supermarketShopView.pinyinSearch(code);
                    }
//                    if(!search_bool) {
//                        search_bool = true;
//                        //1秒后搜索
//                        handler.postDelayed(handlerRunnable, 1000);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                if(!hibe_delete_btn) {
                    hibe_delete_btn = true;
                    return;
                }
                if(hibe_delete_btn) {
                    delete_btn.setVisibility(GONE);
                }

                if (supermarketShopView != null) {
//                    supermarketShopView.clickSelected();
                }
            }
        }
    };

    public void setHibe_delete_btn(boolean a) {
        this.hibe_delete_btn = a;
    }


    @Override
    public void onClick(View v) {
        if (v == delete_btn) {
            search_Input.setText("");
            delete_btn.setVisibility(GONE);
        }
    }

}

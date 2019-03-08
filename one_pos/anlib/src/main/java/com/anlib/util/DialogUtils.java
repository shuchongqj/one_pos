package com.anlib.util;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anlib.widget.dialog.TipClickListener;
import com.anlib.widget.dialog.TipDialog;
import com.one.anlib.R;

/**
 * Created by zhumg on 3/17.
 */
public class DialogUtils {

    /**
     * @param mContext
     * @param translucent 是否透明
     * @param lock        是否鎖定
     * @return
     */
    public static Dialog createLoadingDialog(Context mContext, boolean translucent, final boolean lock) {
        return createLoadingDialog(mContext, "請稍後", translucent, lock);
    }

    /**
     * @param mContext
     * @param msg
     * @param translucent 是否透明
     * @return
     */
    public static Dialog createLoadingDialog(Context mContext, String msg, boolean translucent, final boolean lock) {
        int id = translucent ? R.style.Translucent_NoTitle_Dialog : R.style.NoTitle_Dialog;
        Dialog dialog = new Dialog(mContext, id) {
            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) { //監控/攔截/屏蔽返回鍵
                    if (lock) {
                        return true;
                    }
                } else if (keyCode == KeyEvent.KEYCODE_MENU) {
                    //監控/攔截菜單鍵
                } else if (keyCode == KeyEvent.KEYCODE_HOME) {
                    //由於Home鍵爲系統鍵，此處不能捕獲，需要重寫onAttachedToWindow()
                }
                return super.onKeyDown(keyCode, event);
            }
        };
        dialog.setCanceledOnTouchOutside(!lock);
        dialog.setContentView(R.layout.widget_dialog_loading);
        TextView tv_loading = (TextView) dialog.findViewById(R.id.tv_loading);
        tv_loading.setText(msg);
        return dialog;
    }

    //提示
    public static TipDialog createTipDialog(Context context, String content, String btn) {
        return createTipDialog(context, null, content, null, btn, null);
    }

    //提示
    public static TipDialog createTipDialogOk(Context context, String content, TipClickListener listener) {
        return createTipDialog(context, null, content, null, "確定", listener);
    }

    //提示
    public static TipDialog createTipDialog(Context context, String content, TipClickListener listener) {
        return createTipDialog(context, null, content, "取消", "確定", listener);
    }

    //提示
    public static TipDialog createTipDialog(Context context, String content, String btn, TipClickListener listener) {
        return createTipDialog(context, null, content, null, btn, listener);
    }

    //提示
    public static TipDialog createTipDialog(Context context, String title, String content, String leftBtn, String rightBtn, TipClickListener listener) {
        TipDialog tipDialog = new TipDialog(context);
        if (title == null) {
            title = "溫馨提示";
        }
        tipDialog.setTitle(title);
        tipDialog.setContentMsg(content);
        if (leftBtn == null) {
            tipDialog.hibeLeftBtn();
        } else {
            tipDialog.setLeftBtn(leftBtn);
        }
        tipDialog.setRightBtn(rightBtn);
        tipDialog.setTipClickListener(listener);
        return tipDialog;
    }

    public static Dialog createListDialog(Context mContext, AdapterView.OnItemClickListener listener, BaseAdapter adapter) {

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        Dialog dialog = new Dialog(mContext, R.style.Alert_Dialog_Style);
        View view = View.inflate(mContext, R.layout.widget_dialog_list, null);

        ListView listView = (ListView) view.findViewById(R.id.dl_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);

        dialog.setCanceledOnTouchOutside(false);// 設置點擊屏幕Dialog不消失
        dialog.setContentView(view, new ViewGroup.LayoutParams(dm.widthPixels * 6 / 10, -1));

        return dialog;
    }
}

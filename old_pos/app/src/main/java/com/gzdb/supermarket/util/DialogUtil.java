package com.gzdb.supermarket.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.supermarket.dialog.TipClickListener;
import com.gzdb.supermarket.dialog.TipDialog;


/**
 * Created by yangyifan on 2015/11/11.
 */
public class DialogUtil {
    private static AnimationDrawable mLoadingAinm;

    public static Dialog createImageDialog(Context mContext) {
        Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.layout_imageview);
        return dialog;
    }

    public static Dialog OkAndCancle(Context mContext,String msg) {
        final Dialog dialogs = new Dialog(mContext, R.style.Dialog);
        View view = View.inflate(mContext, R.layout.layout_ok_cancel, null);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        ((TextView) view.findViewById(R.id.text_content)).setText(msg);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });
        dialogs.setContentView(view);
        return dialogs;
    }

    public static Dialog OkAndCancle(Context mContext,String title,String msg) {
        final Dialog dialogs = new Dialog(mContext, R.style.Dialog);
        View view = View.inflate(mContext, R.layout.layout_ok_cancel, null);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        ((TextView) view.findViewById(R.id.text_title)).setText(title);
        TextView textContent= (TextView) view.findViewById(R.id.text_content);
        textContent.setText(Html.fromHtml(msg) );
//        RichText.from(msg).into(textContent);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });
        dialogs.setContentView(view);
        return dialogs;
    }

    public static Dialog progressbar(Context context) {
        final ProgressDialog dialog = new ProgressDialog(context); // 实例化一个进度条对话框
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置进度条风格
        dialog.setTitle("正在加载"); // 设置进度条标题
        dialog.setMessage("数据加载中，请稍后..."); // 设置提示信息
        dialog.setIndeterminate(true); // 设置是否精度显示进度
        dialog.setCancelable(false); // 设置是否触屏取消
//        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消执行",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) { // 设置按钮
//                        dialog.dismiss();
//                    }
//                });
        return dialog;
    }

    public static Dialog loadingDialog(Context mContext, String str_content) {
        Dialog dialog = new Dialog(mContext, R.style.Dialog);
        View view = View.inflate(mContext, R.layout.layout_dialog_shop_cart, null);
        TextView progress_content = (TextView) view.findViewById(R.id.progress_content);
        progress_content.setText(str_content);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.setContentView(view);
        return dialog;
    }

    public static Dialog confirmDialog(Context mContext,String str_content){
        Dialog dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = View.inflate(mContext, R.layout.layout_loading_dialog, null);
        TextView progress_content=(TextView) view.findViewById(R.id.progress_content);
        ImageView iv_loading= (ImageView) view.findViewById(R.id.iv_loading);
        progress_content.setText(str_content);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        mLoadingAinm = (AnimationDrawable) iv_loading.getBackground();
        mLoadingAinm.start();
        dialog.setContentView(view);
        return dialog;
    }

    //提示
    public static TipDialog createTipDialog(Context context, String content, String btn) {
        return createTipDialog(context, null, content, null, btn, null);
    }

    //提示
    public static TipDialog createTipDialog(Context context, String content, TipClickListener listener) {
        return createTipDialog(context, null, content, "取消", "确定", listener);
    }

    //提示
    public static TipDialog createTipDialog(Context context, String content, String btn, TipClickListener listener) {
        return createTipDialog(context, null, content, null, btn, listener);
    }

    //提示
    public static TipDialog createTipDialog(Context context, String title, String content, String leftBtn, String rightBtn, TipClickListener listener) {
        TipDialog tipDialog = new TipDialog(context);
        if (title == null) {
            title = "温馨提示";
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
}
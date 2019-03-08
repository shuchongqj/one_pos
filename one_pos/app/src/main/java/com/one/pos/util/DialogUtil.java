package com.one.pos.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.one.pos.R;

/**
 * Author: even
 * Date:   2019/3/4
 * Description:
 */
public class DialogUtil {


    public static Dialog loadingDialog(Context mContext, String str_content) {
        Dialog dialog = new Dialog(mContext, R.style.Dialog);
        View view = View.inflate(mContext, R.layout.dialog_loading, null);
        TextView progress_content = (TextView) view.findViewById(R.id.progress_content);
        progress_content.setText(str_content);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.setContentView(view);
        return dialog;
    }
}

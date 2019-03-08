package com.gzdb.supermarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.EmptyBean;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Even on 2016/11/24.
 */

public class SendMailDialog extends Dialog {


    private static Context mContext;

    public SendMailDialog(Context context) {
        super(context, R.style.dialog);
    }

    public SendMailDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected SendMailDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }


    public static SendMailDialog create(final Context context, final int itemType, final int sale, final int profit, final int rate, final String typeId, final String itemName, final String start, final String end) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // instantiate the dialog with the custom Theme
        final SendMailDialog dialog = new SendMailDialog(context, R.style.Dialog);
        dialog.setCanceledOnTouchOutside(true);

        View layout = inflater.inflate(R.layout.send_mail, null);


        final EditText mailEt = (EditText) layout.findViewById(R.id.mail_et);
        TextView tvTips = (TextView) layout.findViewById(R.id.tv_tips);
        Button send = (Button) layout.findViewById(R.id.send);
        ImageView close = (ImageView) layout.findViewById(R.id.close);
        tvTips.setText("系统将会发送销售统计报表到填写邮箱地址，导出格式为EXCEL，\n 若没有收到邮件，请查询垃圾邮箱查找或重新点击发送。");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = sDateFormat.format(new Date());
                String mail = mailEt.getText().toString();
                if ("".equals(mail)) {
                    ToastUtil.showToast(context, "请输入邮箱");
                } else {
                    sendMail(dialog, mail, itemType,sale, profit, rate, typeId, itemName, start, end);
                }


            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        dialog.setContentView(layout);
        return dialog;

    }

    private static void sendMail(final Dialog dialog, String email,int itemType,int sale, int profit, int rate, String typeId, String itemName, String start, String end) {
        OkGo.<NydResponse<EmptyBean>>post(Contonts.URL_REPOT)
                .params("email", email)
                .params("itId", typeId)
                .params("itemName", itemName)
                .params("email", email)
                .params("countSort", sale)
                .params("marginSort", profit)
                .params("grossMarginSort", rate)
                .params("createTime", start)
                .params("endTime", end)
                .params("itemType", itemType)
                .execute(new DialogCallback<NydResponse<EmptyBean>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<EmptyBean>> response) {
                        ToastUtil.showSuccess(mContext, "导出成功！");
                        dialog.dismiss();
                    }
                });
    }


    @Override
    public void show() {
        super.show();
    }
}

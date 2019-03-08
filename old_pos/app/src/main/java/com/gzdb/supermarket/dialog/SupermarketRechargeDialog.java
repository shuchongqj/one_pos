package com.gzdb.supermarket.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.UserRechargeBean;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.CreateQRImage;
import com.gzdb.supermarket.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liubolin on 2017/11/24.
 */

public class SupermarketRechargeDialog extends Dialog {

    private int mPayType = 1;

    Bitmap mCodeBitmap1;
    Bitmap mCodeBitmap2;
    Bitmap mCodeBitmap3;

    RadioButton mRadioButton1;
    RadioButton mRadioButton2;
    RadioButton mRadioButton3;

    TextView tv_money;

    ImageView mImgQrCode;

    TextView mTvParam;

    RadioGroup mPayTypeGroup;

    private List<UserRechargeBean> items;

    public SupermarketRechargeDialog(@NonNull Activity context, List<UserRechargeBean> items) {
        super(context, R.style.dialog);
        this.items = items;
        initView(context);
    }

    public void setTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd mm:ss");
        String timeString = f.format(date);
        mTvParam.setText("您的POS机租用时间于 " + timeString + " 到期，请支付续租款继续使用");
    }

    private void initView(Activity context) {
        if (null == items || items.size() < 2) {
            dismiss();
            return;
        }
        setContentView(R.layout.dialog_supermarket_recharge);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        mImgQrCode = (ImageView) findViewById(R.id.img_qr_code);
        mTvParam = (TextView) findViewById(R.id.tv_param);
        mPayTypeGroup = (RadioGroup) findViewById(R.id.pay_type);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_money.setText("￥" + Utils.toYuanStr(items.get(0).getMoney()));
        mPayTypeGroup.check(R.id.pay_type_1);

        mRadioButton1 = (RadioButton) findViewById(R.id.pay_type_1);
        mRadioButton2 = (RadioButton) findViewById(R.id.pay_type_2);
        mRadioButton3 = (RadioButton) findViewById(R.id.pay_type_3);

        mRadioButton1.setText("续租" + items.get(0).getDays() + "天");
        mRadioButton2.setText("续租" + items.get(1).getDays() + "天");
        mRadioButton3.setText("续租" + items.get(2).getDays() + "天");

        /*CreateQRImage img = new CreateQRImage();
        Bitmap bitmap = img.createQRImage(QR_CODE_URL, 1000, 1000);
        mImgQrCode.setImageBitmap(bitmap);*/

        mPayTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.pay_type_1:
                        mPayType = 1;
                        rePayCode();
                        break;
                    case R.id.pay_type_2:
                        mPayType = 2;
                        rePayCode();
                        break;
                    case R.id.pay_type_3:
                        mPayType = 3;
                        rePayCode();
                        break;
                    default:
                        break;
                }
            }
        });

        OkGo.<NydResponse<PayUrlBean>>post(Contonts.URL_PAY_RECHARGE_CODE)
                .params(new HttpParams("passportId", App.getInstance().getCurrentUser().getPassportId()))
                .execute(new JsonCallback<NydResponse<PayUrlBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<PayUrlBean>> response) {
                        if (null == response.body()) {
                            return;
                        }
                        String url = response.body().response.getQrCodeContent();
                        if (TextUtils.isEmpty(url)) {
                            return;
                        }
                        CreateQRImage img = new CreateQRImage();
                        //Bitmap bitmap = img.createQRImage(QR_CODE_URL, 1000, 1000);
                        mCodeBitmap1 = img.createQRImage(url + "/" + items.get(0).getType(), 1000, 1000);
                        mCodeBitmap2 = img.createQRImage(url + "/" + items.get(1).getType(), 1000, 1000);
                        mCodeBitmap3 = img.createQRImage(url + "/" + items.get(2).getType(), 1000, 1000);
                        rePayCode();
                    }
                });

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.7);
        p.width = (int) (d.getWidth() * 0.7);
        getWindow().setAttributes(p);
    }

    private void rePayCode() {
        if (null == items || items.size() < 2)
            return;

        if (mPayType == 1) {
            mImgQrCode.setImageBitmap(mCodeBitmap1);
            tv_money.setText("￥" + Utils.toYuanStr(items.get(0).getMoney()));
        } else if (mPayType == 2) {
            mImgQrCode.setImageBitmap(mCodeBitmap2);
            tv_money.setText("￥" + Utils.toYuanStr(items.get(1).getMoney()));
        } else if (mPayType == 3) {
            mImgQrCode.setImageBitmap(mCodeBitmap3);
            tv_money.setText("￥" + Utils.toYuanStr(items.get(2).getMoney()));
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    class PayUrlBean {
        private String qrCodeContent;

        public String getQrCodeContent() {
            return qrCodeContent;
        }

        public void setQrCodeContent(String qrCodeContent) {
            this.qrCodeContent = qrCodeContent;
        }
    }

}

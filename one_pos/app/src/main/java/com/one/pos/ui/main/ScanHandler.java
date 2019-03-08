package com.one.pos.ui.main;

import android.content.Context;

import com.anlib.util.Arith;
import com.anlib.util.ToastUtils;
import com.one.pos.service.scane.ScanGunKeyEventHelper;

/**
 * @author zhumg
 */
public class ScanHandler implements ScanGunKeyEventHelper.OnScanSuccessListener {

    //搜索
    private boolean search = false;
    // 扫码回调速度
    private static final int FAST_CLICK_DELAY_TIME = 350;
    private long lastScan = 0L;
    //
    private Context context;

    public ScanHandler(Context c) {
        this.context = c;
    }

    @Override
    public void onScanSuccess(String barcode) {
        if (search) {
            return;
        }
        long time = System.currentTimeMillis();
        if (time - lastScan < FAST_CLICK_DELAY_TIME) {
            ToastUtils.showToast(this.context, "您扫得太快了，请放慢速度~");
            return;
        }
        lastScan = time;
        //去掉空格
        String code = barcode.replace(" ", "");

        //折扣
        double discount = 0;
        //重量
        double weight = 0;
        //商品模板ID
        long itemTemplateId = 0;

        //直接查看是否是稳重类商品
        String[] barcodes = explainPrintItemBarcode(code);
        if (barcodes != null) {
            try {
                itemTemplateId = Long.parseLong(barcodes[0]);
                weight = Double.parseDouble(barcodes[1]);
                discount = Double.valueOf(barcodes[2]);
                //通过 itemTemplateId搜索商品，如果搜索不出来，则忽略
            } catch (Exception e) {
            }
        }

    }

    /**
     * 解释 打印的商品条码
     *
     * @return 如果是标品商品，则返回 [商品条码]，如果是非标品打印出来的称重商品，则返回 [商品条码，重量，折扣]
     */
    public String[] explainPrintItemBarcode(String itemBarcode) {
        try {
            int length = itemBarcode.length();
            //非标品，称重类
            String discount = itemBarcode.substring(length - 3, length);
            double discountDouble = Arith.div(Double.parseDouble(discount), 100);
            discount = String.valueOf(discountDouble);
            String weight = itemBarcode.substring(length - 7, length - 3);
            double weightDouble = Arith.div(Double.parseDouble(weight), 100);
            weight = String.valueOf(weightDouble);
            String itemTemplateId = itemBarcode.substring(1, length - 7);
            //如果是原的，则barcode还需要解一次
            return new String[]{itemTemplateId, weight, discount};
        } catch (Exception e) {
            return null;
        }
    }
}

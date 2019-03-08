package com.one.pos.menubar.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anlib.GFragment;
import com.one.pos.R;
import com.zhy.view.flowlayout.TagFlowLayout;

/**
 * Author: even
 * Date:   2019/3/6
 * Description:
 */
public class YctFragment extends GFragment implements View.OnClickListener {
    TagFlowLayout flowlayoutPrice;
    EditText etCardNo;
    EditText etBalance;
    TextView tvPrice;
    EditText etSellerBalance;
    EditText etPreBalance;





    @Override
    protected int getContentViewId() {
        return R.layout.fragment_yct;
    }

    @Override
    protected void initView(View view) {
        initId(view);

    }

    private void initId(View view) {
        flowlayoutPrice = view.findViewById(R.id.flowlayout_price);
        etCardNo = view.findViewById(R.id.et_card_no);
        etBalance = view.findViewById(R.id.et_balance);
        tvPrice = view.findViewById(R.id.tv_price);
        etSellerBalance = view.findViewById(R.id.et_seller_balance);
        etPreBalance = view.findViewById(R.id.et_pre_balance);

    }

    @Override
    public void onClick(View v) {

    }

}

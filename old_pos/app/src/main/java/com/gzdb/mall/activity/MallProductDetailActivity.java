package com.gzdb.mall.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baidu.tts.tools.DateTool;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.mall.adapter.ProductImageAdapter;
import com.gzdb.mall.bean.MallProduct;
import com.gzdb.supermarket.event.RefreshProductEvent;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import org.greenrobot.eventbus.EventBus;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MallProductDetailActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.et_sale_price)
    EditText etSalePrice;
    @Bind(R.id.et_vip_price)
    EditText etVipPrice;
    @Bind(R.id.et_limited)
    EditText etLimited;
    @Bind(R.id.ll_rule)
    LinearLayout llRule;
    @Bind(R.id.tv_start)
    TextView tvStart;
    @Bind(R.id.tv_end)
    TextView tvEnd;
    @Bind(R.id.rv_list)
    RecyclerView rvList;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    private ProductImageAdapter adapter;
    private List<String> list = new ArrayList<>();

    public static final DateFormat sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private TimePickerView pvTime;
    private int timeType = 1;
    private int id, type;
    private String action;
    private MallProduct mallProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_mall_product_detail);
            ButterKnife.bind(this);

            id = getIntent().getIntExtra("id", 0);
            type = getIntent().getIntExtra("type", 0);
            action = getIntent().getStringExtra("action");

            textTitle.setText(action);

            if (action.equals("创建商品")) {
                btnSubmit.setText("创建商品");
            } else {
                btnSubmit.setText("保存");
            }

            if (type == 0) {
                etPrice.setEnabled(false);
                etSalePrice.setEnabled(false);
                etVipPrice.setEnabled(false);
                etLimited.setEnabled(false);
                etPrice.setBackgroundResource(R.drawable.bg_line_gray3);
                etSalePrice.setBackgroundResource(R.drawable.bg_line_gray3);
                etVipPrice.setBackgroundResource(R.drawable.bg_line_gray3);
                etLimited.setBackgroundResource(R.drawable.bg_line_gray3);
            }

            Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            //startDate.set(2013,1,1);
            Calendar endDate = Calendar.getInstance();
            //endDate.set(2020,1,1);

            //正确设置方式 原因：注意事项有说明
            startDate.set(2013, 0, 1);
            endDate.set(2020, 11, 31);
            pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    if (timeType == 0) {
                        tvStart.setText(sSimpleDateFormat.format(date));
                    } else {
                        tvEnd.setText(sSimpleDateFormat.format(date));
                    }
                }
            })
                    .setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitText("确认")//确认按钮文字
                    .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                    .isCyclic(true)//是否循环滚动
                    .setSubmitColor(getResources().getColor(R.color.blue))//确定按钮文字颜色
                    .setCancelColor(getResources().getColor(R.color.gray1))//取消按钮文字颜色
                    .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                    .setRangDate(startDate, endDate)//起始终止年月日设定
                    .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                    .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .isDialog(false)//是否显示为对话框样式
                    .build();

            getData();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        OkGo.<NydResponse<MallProduct>>post(Contonts.MALL_PRODUCT_DETAIL)
                .params("passport_id", app.currentUser.getPassportId())
                .params("item_id", id)
                .execute(new DialogCallback<NydResponse<MallProduct>>(mContext) {
                    @Override
                    public void onSuccess(Response<NydResponse<MallProduct>> response) {
                        try {
                            mallProduct = response.body().response;
                            etName.setText(mallProduct.getNAME());
                            etPrice.setText(Arith.div(mallProduct.getOriginal_price(), 100) + "");
                            etSalePrice.setText(Arith.div(mallProduct.getGroup_price(), 100) + "");
                            etVipPrice.setText(Arith.div(mallProduct.getMember_price(), 100) + "");
                            etLimited.setText(mallProduct.getLimited() + "");
                            tvStart.setText(DateTool.format(mallProduct.getStart_time(), "yyyy-MM-dd HH:mm"));
                            tvEnd.setText(DateTool.format(mallProduct.getEnd_time(), "yyyy-MM-dd HH:mm"));
                            String imgArray[] = mallProduct.getImgs().split(",");
                            for (int i = 0; i < imgArray.length; i++) {
                                list.add(imgArray[i]);
                            }
                            adapter = new ProductImageAdapter(mContext, list);
                            rvList.setLayoutManager(new GridLayoutManager(mContext, 4) {
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            });
                            rvList.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void edit(int type) {
        String url=Contonts.MALL_PRODUCT_EDIT;
        if(type==1){
            url=Contonts.MALL_PRODUCT_SELETCT;
        }
        String original_price = etPrice.getText().toString();
        String group_price = etSalePrice.getText().toString();
        String member_price = etVipPrice.getText().toString();
        String limited = etLimited.getText().toString();
        String start_time = tvStart.getText().toString();
        String end_time = tvEnd.getText().toString();
        if (original_price.equals("")) {
            ToastUtil.showToast(mContext, "原始价格不能为空");
            return;
        }
        if (group_price.equals("")) {
            ToastUtil.showToast(mContext, "优选价不能为空");
            return;
        }
        if (member_price.equals("")) {
            ToastUtil.showToast(mContext, "会员优选价不能为空");
            return;
        }
        if (limited.equals("")) {
            ToastUtil.showToast(mContext, "限购数量不能为空");
            return;
        }
        if (start_time.equals("")) {
            ToastUtil.showToast(mContext, "开始时间不能为空");
            return;
        }
        if (end_time.equals("")) {
            ToastUtil.showToast(mContext, "结束时间不能为空");
            return;
        }
        if(Double.valueOf(group_price)>Double.valueOf(original_price)){
            ToastUtil.showToast(mContext, "优选价不能大于原价");
            return;
        }
        if(Double.valueOf(member_price)>Double.valueOf(group_price)){
            ToastUtil.showToast(mContext, "会员优选价不能大于优选价");
            return;
        }
        OkGo.<NydResponse<Object>>post(url)
                .params("passport_id", app.currentUser.getPassportId())
                .params("item_id", id)
                .params("original_price", Arith.mul(Double.valueOf(original_price), 100))
                .params("group_price", Arith.mul(Double.valueOf(group_price), 100))
                .params("member_price", Arith.mul(Double.valueOf(member_price), 100))
                .params("limited", limited)
                .params("start_time", tvStart.getText().toString())
                .params("end_time", tvEnd.getText().toString())
                .execute(new DialogCallback<NydResponse<Object>>(mContext) {
                    @Override
                    public void onSuccess(Response<NydResponse<Object>> response) {

                        EventBus.getDefault().post(new RefreshProductEvent());
                        finish();
                    }

                    @Override
                    public void onError(Response<NydResponse<Object>> response) {
                      //  ToastUtil.showToast(mContext, "该模版已使用");
                        super.onError(response);
                    }
                });
    }

    @OnClick({R.id.img_back, R.id.tv_start, R.id.tv_end, R.id.btn_cancel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_start:
                timeType = 0;
                Date date = new Date(mallProduct.getStart_time());
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                pvTime.setDate(c);
                pvTime.show();
                break;
            case R.id.tv_end:
                timeType = 1;
                Date date2 = new Date(mallProduct.getEnd_time());
                Calendar c2 = Calendar.getInstance();
                c2.setTime(date2);
                pvTime.setDate(c2);
                pvTime.show();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_submit:
                if (action.equals("创建商品")) {
                    edit(1);
                }else{
                    edit(0);
                }
                break;
        }
    }
}

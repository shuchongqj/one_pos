package com.gzdb.supermarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.gzdb.basepos.R;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.supermarket.event.PrintTypeEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yunshi on 2017/11/20.
 */

public class SelectPrintDialog extends Dialog {

    @Bind(R.id.rbtn_yundou)
    RadioButton rbtnYundou;
    @Bind(R.id.rbtn_duimi)
    RadioButton rbtnDuimi;
    @Bind(R.id.rbtn_yundou2)
    RadioButton rbtnYundou2;
    @Bind(R.id.rbtn_yundou3)
    RadioButton rbtnYundou3;
    @Bind(R.id.rbtn_duimi2)
    RadioButton rbtnDuimi2;
    @Bind(R.id.rbtn_life)
    RadioButton rbtnLife;
    @Bind(R.id.rbtn_barcode)
    RadioButton rbtnBarcode;


    @Bind(R.id.rbtn_barcode_label)
    RadioButton rbtnBarcodeLabel;
    private View.OnClickListener mListener;

    private Context mContext;

    public SelectPrintDialog(@NonNull Context context, View.OnClickListener listener) {
        super(context, R.style.dialog);
        this.mListener = listener;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_print);
        ButterKnife.bind(this);
        int printType = SPUtil.getInstance().getInt(mContext, "print_type");
        switch (printType) {
            case 0:
                rbtnYundou.setChecked(true);
                break;
            case 1:
                rbtnDuimi.setChecked(true);
                break;
            case 2:
                rbtnYundou2.setChecked(true);
                break;
            case 3:
                rbtnDuimi2.setChecked(true);
                break;
            case 4:
                rbtnLife.setChecked(true);
                break;
            case 5:
                rbtnYundou3.setChecked(true);
                break;
            case 6:
                rbtnBarcode.setChecked(true);
                break;
            case 7:
                rbtnBarcodeLabel.setChecked(true);
                break;
        }

        if (mListener == null) {
            findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        } else {
            findViewById(R.id.btn_commit).setOnClickListener(mListener);
        }

        rbtnYundou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SPUtil.getInstance().putInt(mContext, "print_type", 0);
                    EventBus.getDefault().post(new PrintTypeEvent());
                }
            }
        });

        rbtnDuimi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SPUtil.getInstance().putInt(mContext, "print_type", 1);
                    EventBus.getDefault().post(new PrintTypeEvent());
                }
            }
        });

        rbtnYundou2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SPUtil.getInstance().putInt(mContext, "print_type", 2);
                    EventBus.getDefault().post(new PrintTypeEvent());
                }
            }
        });

        rbtnDuimi2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SPUtil.getInstance().putInt(mContext, "print_type", 3);
                    EventBus.getDefault().post(new PrintTypeEvent());
                }
            }
        });

        rbtnLife.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SPUtil.getInstance().putInt(mContext, "print_type", 4);
                    EventBus.getDefault().post(new PrintTypeEvent());
                }
            }
        });

        rbtnYundou3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SPUtil.getInstance().putInt(mContext, "print_type", 5);
                    EventBus.getDefault().post(new PrintTypeEvent());
                }
            }
        });

        rbtnBarcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SPUtil.getInstance().putInt(mContext, "print_type", 6);
                    EventBus.getDefault().post(new PrintTypeEvent());
                }
            }
        });

        rbtnBarcodeLabel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SPUtil.getInstance().putInt(mContext, "print_type", 7);
                    EventBus.getDefault().post(new PrintTypeEvent());
                }
            }
        });

    }
}

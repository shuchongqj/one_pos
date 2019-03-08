package com.gzdb.supermarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.gzdb.basepos.R;
import com.gzdb.supermarket.adapter.PlaceOrderAdapter;
import com.gzdb.supermarket.entity.PlaceOrderDataList;

/**
 * Created by Even on 2016/6/13.
 */
public class PlaceOrderDialog extends Dialog{


    public PlaceOrderDialog(Context context) {
        super(context);
    }

    public PlaceOrderDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PlaceOrderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        super.show();
    }



        public static PlaceOrderDialog create(Context context, PlaceOrderDataList pod) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // instantiate the dialog with the custom Theme
            final PlaceOrderDialog dialog = new PlaceOrderDialog(context, R.style.Dialog);
            dialog.setCanceledOnTouchOutside(true);

            View layout = inflater.inflate(R.layout.layout_place_oder_dialog, null);
            ListView placeOrderTip = (ListView) layout.findViewById(R.id.place_order_tip);
            Button submit = (Button) layout.findViewById(R.id.submit);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            PlaceOrderAdapter adapter = new PlaceOrderAdapter(context, pod.getMenuList());
            placeOrderTip.setAdapter(adapter);

            dialog.setContentView(layout);
            return dialog;
        }

}

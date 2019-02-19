package com.anlib.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.anlib.util.DateTimeUtils;
import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Calendar;


/**
 * 日期，时间，选择器，IOS 风格
 */
public class DateTimePicker extends TimePickerView {

    private DateTimePicker(PickerOptions pickerOptions) {
        super(pickerOptions);
        Dialog mDialog = getDialog();
        if (mDialog == null) {
            return;
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM);

        params.leftMargin = 0;
        params.rightMargin = 0;
        getDialogContainerLayout().setLayoutParams(params);

        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            //修改动画样式
            dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);
            //改成Bottom,底部显示
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
    }

    public void setCenterTitle(String title) {
        setCenterTitle(title, null);
    }

    public void setCenterTitle(String title, View.OnClickListener listener) {
        TextView tvTitle = (TextView) findViewById(com.bigkoo.pickerview.R.id.tvTitle);
        if (tvTitle != null) {
            tvTitle.setText(title);
            if (listener != null) {
                tvTitle.setOnClickListener(listener);
            }
        }
    }

    public static DateTimePicker createDatePicker(Context context, OnTimeSelectListener onTimeSelectListener) {
        return createDatePicker(context, onTimeSelectListener, null, null);
    }

    public static DateTimePicker createTimePicker(Context context, OnTimeSelectListener onTimeSelectListener, String startTime, String endTime) {
        PickerOptions mPickerOptions =
                createPickerOptions(PickerOptions.TYPE_PICKER_TIME, context,
                        onTimeSelectListener, new boolean[]{false, false, false, true, true, true},
                        startTime, endTime);
        return new DateTimePicker(mPickerOptions);
    }

    public static DateTimePicker createTimePicker(Context context, OnTimeSelectListener onTimeSelectListener) {
        return createTimePicker(context, onTimeSelectListener, null, null);
    }

    public static DateTimePicker createDatePicker(Context context, OnTimeSelectListener onTimeSelectListener, String startDate, String endDate) {
        PickerOptions mPickerOptions =
                createPickerOptions(PickerOptions.TYPE_PICKER_TIME, context,
                        onTimeSelectListener, new boolean[]{true, true, true, false, false, false},
                        startDate, endDate);
        return new DateTimePicker(mPickerOptions);
    }

    private static PickerOptions createPickerOptions(int pickerType, Context context, OnTimeSelectListener listener, boolean[] type, String startDate, String endDate) {
        PickerOptions pickerOptions = new PickerOptions(PickerOptions.TYPE_PICKER_TIME);
        pickerOptions.context = context;
        pickerOptions.timeSelectListener = listener;
        pickerOptions.type = type;
        pickerOptions.isDialog = true;

        if (startDate != null) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(DateTimeUtils.strToDate_ymd(startDate));
            pickerOptions.startDate = startCalendar;

        }
        if (endDate != null) {
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(DateTimeUtils.strToDate_ymd(endDate));
            pickerOptions.startDate = endCalendar;
        }

        return pickerOptions;
    }
}

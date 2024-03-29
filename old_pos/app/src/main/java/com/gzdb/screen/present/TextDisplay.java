package com.gzdb.screen.present;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.screen.BasePresentation;

/**
 * Created by highsixty on 2018/3/23.
 * mail  gaolulin@sunmi.com
 */

public class TextDisplay extends BasePresentation {

    private TextView tv;

    public TextDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vice_text_layout);
        tv = (TextView) findViewById(R.id.tv);
    }

    public void update(String tip) {
        tv.setText(tip);
    }
}

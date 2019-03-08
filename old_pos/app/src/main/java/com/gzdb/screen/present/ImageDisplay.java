package com.gzdb.screen.present;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gzdb.basepos.R;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.screen.BasePresentation;

/**
 * Created by highsixty on 2018/3/23.
 * mail  gaolulin@sunmi.com
 */

public class ImageDisplay extends BasePresentation {

    private ImageView iv_image;

    public ImageDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vice_image_layout);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        Glide.with(getContext()).load(SPUtil.getInstance().getString(getContext(),"screen_ad","http://oss.0085.com/2018/0801/positems/15331234120641.jpeg")).into(iv_image);
    }

    public void update(String url) {
        Glide.with(getContext()).load(url).into(iv_image);
    }
}

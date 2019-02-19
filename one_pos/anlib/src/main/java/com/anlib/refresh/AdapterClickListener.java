package com.anlib.refresh;

import android.widget.AdapterView;

/**
 * Created by zhumg on 3/23.
 */
public interface AdapterClickListener<DATA> {
    void onItemClick(AdapterView<?> adapterView, DATA model, int position);
}

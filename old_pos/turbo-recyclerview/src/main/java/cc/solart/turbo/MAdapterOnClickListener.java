package cc.solart.turbo;

import android.support.v7.widget.RecyclerView;

/**
 * Created by nongyd on 17/5/3.
 */

public interface MAdapterOnClickListener<T>  {

    void onItemClick(RecyclerView.ViewHolder vh, T item, int position);
}

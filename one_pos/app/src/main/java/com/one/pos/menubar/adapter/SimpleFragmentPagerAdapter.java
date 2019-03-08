package com.one.pos.menubar.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.one.pos.R;
import com.one.pos.menubar.BadgeView;

import java.util.List;

/**
 * Author: SherlockShi
 * Date:   2016-11-01 17:38
 * Description:
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private FragmentManager fragmentManager;
    private List<Fragment> mFragmentList;
    private List<String> mPageTitleList;
    private List<Integer> mBadgeCountList;

    public SimpleFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList, List<String> pageTitleList,
                                      List<Integer> badgeCountList) {
        super(fragmentManager);
        this.fragmentManager=fragmentManager;
        this.mFragmentList = fragmentList;
        this.mPageTitleList = pageTitleList;
        this.mBadgeCountList = badgeCountList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageTitleList.get(position);
    }

    public View getTabItemView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_layout_item, null);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mPageTitleList.get(position));

        View target = view.findViewById(R.id.badgeview_target);

        BadgeView badgeView = new BadgeView(mContext);
        badgeView.setTargetView(target);
        badgeView.setBadgeMargin(100, 3, 0, 0);
        badgeView.setTextSize(10);
        badgeView.setText(formatBadgeNumber(mBadgeCountList.get(position)));

        return view;
    }

    public static String formatBadgeNumber(int value) {
            return Integer.toString(value);

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}

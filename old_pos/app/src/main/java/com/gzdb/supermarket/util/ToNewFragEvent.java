package com.gzdb.supermarket.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by nongyudi on 2017/3/24.
 */

public class ToNewFragEvent {
    protected String Tag;
    protected Fragment fragment;
    protected boolean isReplace;
    protected Bundle bundle;
    protected boolean checkNet;
    public ToNewFragEvent() {
    }
    public ToNewFragEvent(String tag, Fragment fragment, Bundle bundle, boolean checkNet, boolean replace) {
        Tag = tag;
        this.fragment = fragment;
        this.bundle=bundle;
        this.checkNet=checkNet;
        this.isReplace=replace;
    }
    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public boolean isReplace() {
        return isReplace;
    }

    public void setReplace(boolean replace) {
        isReplace = replace;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}

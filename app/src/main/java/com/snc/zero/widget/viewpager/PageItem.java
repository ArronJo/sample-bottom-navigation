package com.snc.zero.widget.viewpager;

import java.io.Serializable;

import androidx.fragment.app.Fragment;

/**
 * ViewPager2 Adapter Item
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class PageItem implements Serializable {

    final private Fragment fragment;

    public PageItem(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return this.fragment;
    }
}

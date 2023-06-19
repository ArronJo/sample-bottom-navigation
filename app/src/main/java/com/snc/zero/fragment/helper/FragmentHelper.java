package com.snc.zero.fragment.helper;

import com.snc.zero.log.Logger;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Fragment Helper
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class FragmentHelper {
    private static final String TAG = FragmentHelper.class.getSimpleName();

    private List<Fragment> fragmentList;

    private final FragmentManager fragmentManager;

    public FragmentHelper(FragmentActivity fragmentActivity) {
        this(fragmentActivity, null);
    }

    public FragmentHelper(FragmentActivity fragmentActivity, List<Fragment> fragmentList) {
        this.fragmentManager = fragmentActivity.getSupportFragmentManager();
        this.fragmentList = fragmentList;
        if (null == this.fragmentList) {
            this.fragmentList = new ArrayList<>();
        }
    }

    public Fragment get(int position) {
        if (0 == this.fragmentList.size()) {
            return null;
        }
        return this.fragmentList.get(position);
    }

    public Fragment getCurrentFragment() {
        if (0 == this.fragmentList.size()) {
            return null;
        }
        return get(this.fragmentList.size() - 1);
    }

    public void show(int layoutResId, Fragment fragment) {
        this.fragmentList.add(fragment);
        this.fragmentManager.beginTransaction()
                .add(layoutResId, fragment)
                .show(fragment)
                .commitAllowingStateLoss();
    }

    public void remove(Fragment fragment) {
        try {
            if (null == fragment || fragment.isRemoving()) {
                return;
            }
            this.fragmentList.remove(fragment);
            this.fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Logger.e(TAG, e);
        }
    }

    @SuppressWarnings("unused")
    public void remove() {
        remove(getCurrentFragment());
    }

    public int size() {
        return this.fragmentList.size();
    }

}

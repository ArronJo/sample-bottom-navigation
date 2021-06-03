package com.snc.zero.fragment.helper;

import com.snc.zero.log.Logger;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Fragment Helper
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class FragmentHelper {
    private static final String TAG = FragmentHelper.class.getSimpleName();

    private final int containerViewId;
    private List<Fragment> fragmentList;

    private final FragmentManager fragmentManager;
    private int showPosition = -1;

    public FragmentHelper(FragmentActivity fragmentActivity, int containerViewId) {
        this(fragmentActivity, containerViewId, null);
    }

    public FragmentHelper(FragmentActivity fragmentActivity, int containerViewId, List<Fragment> fragmentList) {
        this.fragmentManager = fragmentActivity.getSupportFragmentManager();
        this.containerViewId = containerViewId;
        this.fragmentList = fragmentList;
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
        if (null == this.fragmentList) {
            this.fragmentList = new ArrayList<>();
        }
    }

    public Fragment get(int position) {
        return this.fragmentList.get(position);
    }

    public Fragment getCurrentFragment() {
        return get(this.showPosition);
    }

    public void show(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = this.fragmentList.get(position);

        if (!fragment.isAdded()) {
            try {
                transaction.add(this.containerViewId, fragment);
            } catch (IllegalStateException e) {
                Logger.e(TAG, e);
                return;
            }
        }

        for (int i = 0; i < this.fragmentList.size(); i++) {
            if (i != position) {
                Fragment f = this.fragmentList.get(i);
                transaction.hide(f);
            }
        }

        if (fragment.isRemoving()) {
            return;
        }

        transaction.show(fragment);
        transaction.commit();
        this.showPosition = position;
    }

    public void show(int layoutResId, Fragment fragment) {
        fragmentManager.beginTransaction()
                .add(layoutResId, fragment)
                .show(fragment)
                .commit();
    }

    public void remove(Fragment fragment) {
        try {
            if (null == fragment || fragment.isRemoving()) {
                return;
            }
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit();
        } catch (IllegalStateException e) {
            Logger.e(TAG, e);
        }
    }

}

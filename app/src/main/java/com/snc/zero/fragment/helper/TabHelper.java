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
public class TabHelper {
    private static final String TAG = TabHelper.class.getSimpleName();

    private final int containerViewId;
    private List<Fragment> fragmentList;

    private final FragmentManager fragmentManager;
    private int showTabPosition = -1;

    public TabHelper(FragmentActivity fragmentActivity, int containerViewId) {
        this(fragmentActivity, containerViewId, null);
    }

    public TabHelper(FragmentActivity fragmentActivity, int containerViewId, List<Fragment> fragmentList) {
        this.fragmentManager = fragmentActivity.getSupportFragmentManager();
        this.containerViewId = containerViewId;
        this.fragmentList = fragmentList;
        if (null == this.fragmentList) {
            this.fragmentList = new ArrayList<>();
        }
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

    public Fragment get(int position) {
        if (null == this.fragmentList || 0 == this.fragmentList.size()) {
            return null;
        }
        return this.fragmentList.get(position);
    }

    public Fragment getCurrentTab() {
        return get(this.showTabPosition);
    }

    public void showTab(int position) {
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

        this.showTabPosition = position;
    }

}

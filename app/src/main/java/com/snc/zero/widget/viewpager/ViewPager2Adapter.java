package com.snc.zero.widget.viewpager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * ViewPager2 Adapter
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class ViewPager2Adapter extends FragmentStateAdapter {

    private List<PageItem> items;

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return getItem(position).getFragment();
    }

    @Override
    public int getItemCount() {
        return null != this.items ? this.items.size() : 0;
    }

    public void setItems(List<PageItem> items) {
        this.items = items;
    }

    public PageItem getItem(int position) {
        return 0 != getItemCount() ? this.items.get(position) : null;
    }
}

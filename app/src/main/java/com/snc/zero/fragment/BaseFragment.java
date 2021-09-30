package com.snc.zero.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;

import com.snc.zero.activity.BaseActivity;
import com.snc.zero.fragment.listener.InteractionListener;
import com.snc.zero.fragment.helper.FragmentHelper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Abstract base fragment
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public abstract class BaseFragment extends Fragment {
    private InteractionListener listener;
    private FragmentHelper fragmentHelper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof InteractionListener) {
            listener = (InteractionListener) context;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public Fragment getFragment() {
        return this;
    }

    public void setFragmentHelper(FragmentHelper helper) {
        fragmentHelper = helper;
    }

    public FragmentHelper getFragmentHelper() {
        return fragmentHelper;
    }

    @SuppressLint("UseRequireInsteadOfGet")
    @SuppressWarnings("SameParameterValue")
    protected void postDelayed(Runnable r, long delayMillis) {
        ((BaseActivity) requireActivity()).postDelayed(r, delayMillis);
    }

    /**
     * Fragment 이벤트를 부모 Activity 에게 전달하기 위한 리스너
     * @param listener - 리스너
     */
    public void setInteractionListener(InteractionListener listener) {
        this.listener = listener;
    }

    public InteractionListener getInteractionListener() {
        return this.listener;
    }

    /*
    public void showProgress() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgress();
        }
    }

    public void hideProgress() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideProgress();
        }
    }

    public void showProgressInside() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgressInside();
        }
    }

    public void hideProgressInside() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideProgressInside();
        }
    }
     */
}

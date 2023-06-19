package com.snc.zero.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;

import com.snc.zero.activity.BaseActivity;
import com.snc.zero.fragment.helper.FragmentHelper;
import com.snc.zero.fragment.listener.MessageListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * Abstract base fragment
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public abstract class BaseFragment extends Fragment {
    private MessageListener listener;
    private FragmentHelper fragmentHelper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MessageListener) {
            listener = (MessageListener) context;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public FragmentActivity getParentActivity() {
        return requireActivity();
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
    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    public boolean sendMessage(String command, Object...params) {
        if (null == this.listener) {
            return false;
        }
        this.listener.onMessage(getFragment(), command, params);
        return true;
    }

}

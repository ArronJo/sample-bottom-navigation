package com.snc.sample.bottom_navigation.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.snc.sample.bottom_navigation.R;
import com.snc.sample.bottom_navigation.ui.fragment.web.WebFragment;
import com.snc.zero.activity.BaseActivity;
import com.snc.zero.fragment.BaseFragment;
import com.snc.zero.fragment.helper.FragmentHelper;
import com.snc.zero.log.Logger;
import com.snc.zero.util.IntentUtil;

/**
 * WebViewActivity
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class WebFragmentActivity extends BaseActivity {
    private static final String TAG = WebFragmentActivity.class.getSimpleName();

    private FragmentHelper mFragmentHelper;

    //////////////////////////////////////////////

    private BaseFragment getCurrentFragment() {
        if (mFragmentHelper.getCurrentFragment() instanceof BaseFragment) {
            return (BaseFragment) mFragmentHelper.getCurrentFragment();
        }
        return null;
    }

    //////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        mFragmentHelper = new FragmentHelper(getActivity());

        showTitleWeb();
    }

    //////////////////////////////////////////////

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            if (KeyEvent.KEYCODE_BACK == keyCode) {
                if (null != getCurrentFragment()) {
                    if (getCurrentFragment().onKeyDown(keyCode, event)) {
                        return true;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //////////////////////////////////////////////

    public void showTitleWeb() {
        Logger.i(TAG, "showTitleWebView()");

        String title = IntentUtil.getStringExtra(getIntent(), "title", "");
        String url = IntentUtil.getStringExtra(getIntent(), "url", "");

        final WebFragment webFragment = new WebFragment();
        webFragment.setFragmentHelper(mFragmentHelper);
        webFragment.setTitle(title);
        webFragment.setUrl(url);
        webFragment.setMessageListener((fragment, command, params) -> {
            if ("finish".equalsIgnoreCase(command)) {
                finish();
            }
        });

        mFragmentHelper.show(R.id.base_layout, webFragment);
    }

    //////////////////////////////////////////////

}
package com.snc.sample.bottom_navigation.ui.fragment.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.snc.sample.bottom_navigation.BuildConfig;
import com.snc.sample.bottom_navigation.R;
import com.snc.sample.bottom_navigation.sharedpreference.PrefConst;
import com.snc.sample.bottom_navigation.ui.fragment.splash.SplashFragment;
import com.snc.sample.bottom_navigation.ui.webview.helper.WebViewHelper;
import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.fragment.BaseFragment;
import com.snc.zero.json.helper.JSONHelper;
import com.snc.zero.sharedpreference.PrefEditor;
import com.snc.zero.util.ActivityUtil;
import com.snc.zero.util.StringUtil;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Title 이 있는 Web Fragment
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class WebFragment extends BaseFragment {
    //private static final String TAG = Tab1Fragment.class.getSimpleName();

    private WebView webview;
    private String urlString;
    private String title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onDestroyView() {
        WebViewHelper.removeWebView(this.webview);
        this.webview = null;
        super.onDestroyView();
    }

    @SuppressLint("AddJavascriptInterface")
    private void init(View view) {
        final ViewGroup contentView = view.findViewById(R.id.contentView);
        if (null == contentView) {
            DialogBuilder.with(getActivity())
                    .setMessage("The contentView does not exist.")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> ActivityUtil.finish(getActivity()))
                    .show();
            return;
        }

        TextView titleView = view.findViewById(R.id.title);
        titleView.setText(StringUtil.nvl(this.title, ""));

        ImageView closeView = view.findViewById(R.id.close);
        closeView.setOnClickListener(v1 -> {
            if (!sendMessage("finish", (Object[]) null)) {
                getFragmentHelper().remove(getFragment());
            }
        });

        ImageView backView = view.findViewById(R.id.back);
        backView.setOnClickListener(v1 -> {
            if (!sendMessage( "finish", (Object[]) null)) {
                getFragmentHelper().remove(getFragment());
            }
        });

        this.webview = WebViewHelper.addWebView(requireContext(), contentView);
        WebViewHelper.setupClient(this.webview);

        boolean isShowProgress = true;
        if (isShowProgress && BuildConfig.FEATURE_SPLASH) {
            if (getFragmentHelper().getCurrentFragment() instanceof SplashFragment) {
                isShowProgress = false;
            }
        }
        if (isShowProgress && BuildConfig.FEATURE_ONBOARDING) {
            PrefEditor pref = new PrefEditor(requireContext());
            if (!"true".equals(pref.getString(PrefConst.DID_ON_BOARDING, ""))) {
                isShowProgress = false;
            }
        }
        if (isShowProgress && BuildConfig.FEATURE_COACH) {
            PrefEditor pref = new PrefEditor(requireContext());
            if (!"true".equals(pref.getString(PrefConst.DID_ON_COACH_GUIDE, ""))) {
                isShowProgress = false;
            }
        }

        JSONObject obj = new JSONObject();
        JSONHelper.put(obj, "showProgress", isShowProgress);
        this.webview.setTag(obj);

        WebViewHelper.loadUrl(this.webview, StringUtil.nvl(this.urlString, ""));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction()) {
            if (KeyEvent.KEYCODE_BACK == keyCode) {
                if (this.webview.canGoBack()) {
                    this.webview.goBack();
                    return true;
                }

                if (!sendMessage("finish", (Object[]) null)) {
                    getFragmentHelper().remove(getFragment());
                }
                return true;
            }
        }
        return false;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String urlString) {
        this.urlString = urlString;
    }

}

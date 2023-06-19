package com.snc.sample.bottom_navigation.ui.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.snc.sample.bottom_navigation.R;
import com.snc.sample.bottom_navigation.ui.webview.helper.WebViewHelper;
import com.snc.zero.activity.BaseActivity;
import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.json.helper.JSONHelper;
import com.snc.zero.log.Logger;
import com.snc.zero.util.ActivityUtil;
import com.snc.zero.util.IntentUtil;
import com.snc.zero.util.StringUtil;

import org.json.JSONObject;

/**
 * WebViewActivity
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class WebActivity extends BaseActivity {
    private static final String TAG = WebActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        init();
    }

    private void init() {
        String title = IntentUtil.getStringExtra(getIntent(), "title", "");
        String url = IntentUtil.getStringExtra(getIntent(), "url", "");

        if (StringUtil.isEmpty(url)) {
            DialogBuilder.with(getActivity())
                    .setMessage("The url does not exist.")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> ActivityUtil.finish(getActivity()))
                    .show();
            return;
        }

        final ViewGroup contentView = findViewById(R.id.contentView);
        if (null == contentView) {
            DialogBuilder.with(getActivity())
                    .setMessage("The contentView does not exist.")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> ActivityUtil.finish(getActivity()))
                    .show();
            return;
        }

        TextView titleView = findViewById(R.id.title);
        titleView.setText(title);

        ImageView closeView = findViewById(R.id.close);
        closeView.setOnClickListener(v1 -> {
            Logger.i(TAG, "onclick : closeView");
            finish();
        });

        ImageView backView = findViewById(R.id.back);
        backView.setOnClickListener(v1 -> {
            Logger.i(TAG, "onclick : backView");
            finish();
        });

        WebView webview = WebViewHelper.addWebView(getContext(), contentView);
        WebViewHelper.setupClient(webview);

        JSONObject obj = new JSONObject();
        JSONHelper.put(obj, "showProgress", true);
        webview.setTag(obj);

        WebViewHelper.loadUrl(webview, url);
    }

}
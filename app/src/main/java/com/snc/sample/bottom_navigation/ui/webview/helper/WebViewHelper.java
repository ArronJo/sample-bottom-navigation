package com.snc.sample.bottom_navigation.ui.webview.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.snc.sample.bottom_navigation.sharedpreference.PrefConst;
import com.snc.sample.bottom_navigation.ui.webview.bridge.AndroidBridge;
import com.snc.zero.webview.listener.CSFileChooserListener;
import com.snc.zero.json.helper.JSONHelper;
import com.snc.zero.log.Logger;
import com.snc.zero.permission.RPermission;
import com.snc.zero.permission.RPermissionListener;
import com.snc.zero.sharedpreference.PrefEditor;
import com.snc.zero.util.PackageUtil;
import com.snc.zero.util.StringUtil;
import com.snc.zero.webview.client.CSWebChromeClient;
import com.snc.zero.webview.client.CSWebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WebView Helper
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class WebViewHelper {
    private static final String TAG = WebViewHelper.class.getSimpleName();

    private static final String SCHEME_HTTP = "http://";
    private static final String SCHEME_HTTPS = "https://";
    private static final String SCHEME_FILE = "file://";

    public static WebView addWebView(Context context, ViewGroup parentView) {
        WebView webView = newWebView(context);
        parentView.addView(webView);
        return webView;
    }

    public static void removeWebView(WebView webView) {
        if (null != webView) {
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
        }
    }

    public static WebView newWebView(Context context) {
        WebView webView = new WebView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        webView.setFadingEdgeLength(0);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // setup
        setup(webView);

        return webView;
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    public static void setup(WebView webView) {
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }

        settings.setGeolocationEnabled(true);

        settings.setSupportMultipleWindows(true);

        String ua = makeUserAgent(webView);
        settings.setUserAgentString(ua);
    }

    public static void setupClient(WebView webView) {
        CSWebViewClient csWebViewClient = new CSWebViewClient(webView.getContext());
        webView.setWebViewClient(csWebViewClient);

        CSWebChromeClient webChromeClient = new CSWebChromeClient(webView.getContext());
        webView.setWebChromeClient(webChromeClient);

        CSFileChooserListener fileChooserListener = new CSFileChooserListener(webView.getContext());
        webChromeClient.setFileChooserListener(fileChooserListener);

        webView.addJavascriptInterface(new AndroidBridge(webView), "AndroidBridge");
    }

    public static String makeUserAgent(WebView webView) {
        String ua = webView.getSettings().getUserAgentString();
        try {
            ua += !ua.endsWith(" ") ? " " : "";
            ua += "SNC/";
            ua += PackageUtil.getPackageVersionName(webView.getContext()) + "." + PackageUtil.getPackageVersionCode(webView.getContext());
            return ua;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, e);
        }
        return ua;
    }

    public static void loadUrl(final WebView webView, final String uriString) {
        if (null == webView) {
            Logger.e(TAG, "webview is null");
            return;
        }

        Logger.i(TAG, "loadUrl  = " + uriString);

        final Map<String, String> extraHeaders = new HashMap<>();
        //extraHeaders.put("SNC-OS", "A");

        PrefEditor pref = new PrefEditor(webView.getContext());
        String loginInfo = pref.getString(PrefConst.GET_LOGIN_INFO, "");
        Logger.i(TAG, "loginInfo  = " + loginInfo);

        if (!StringUtil.isEmpty(loginInfo)) {
            try {
                JSONObject loginObj = new JSONObject(loginInfo);
                String authToken = JSONHelper.getString(loginObj, "accessToken", "");
                extraHeaders.put("SNC-Authorization", authToken);
            } catch (JSONException e) {
                Logger.e(TAG, e);
            }
        }

        if (uriString.startsWith(SCHEME_HTTP) || uriString.startsWith(SCHEME_HTTPS)) {
            webView.loadUrl(uriString, extraHeaders);
        }
        else if (uriString.startsWith(SCHEME_FILE)) {
            List<String> permissions = new ArrayList<>();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            RPermission.with(webView.getContext())
                    .setPermissionListener(new RPermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Logger.i(TAG, "[WEBVIEW] onPermissionGranted()");
                            webView.loadUrl(uriString, extraHeaders);
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions, int status) {
                            Logger.e(TAG, "[WEBVIEW] onPermissionDenied()..." + deniedPermissions.toString());
                        }
                    })
                    .setPermissions(permissions)
                    .check();
        }
    }

}

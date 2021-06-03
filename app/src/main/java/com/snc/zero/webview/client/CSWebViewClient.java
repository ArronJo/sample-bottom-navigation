package com.snc.zero.webview.client;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.snc.sample.bottom_navigation.ui.webview.helper.WebViewHelper;
import com.snc.zero.activity.BaseActivity;
import com.snc.zero.json.helper.JSONHelper;
import com.snc.zero.log.Logger;
import com.snc.zero.util.IntentUtil;

import org.json.JSONObject;

import java.net.URISyntaxException;

import androidx.annotation.RequiresApi;
import androidx.webkit.WebViewAssetLoader;

/**
 * WebViewClient
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class CSWebViewClient extends WebViewClient {
    private static final String TAG = CSWebViewClient.class.getSimpleName();

    private final WebViewAssetLoader assetLoader;

    public CSWebViewClient(Context context) {
        this.assetLoader = new WebViewAssetLoader.Builder()
                .setDomain("asset.snc.com")
                .addPathHandler("/res/", new WebViewAssetLoader.ResourcesPathHandler(context))
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(context))
                .build();
    }

    @Override
    @RequiresApi(21)
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        Logger.d(TAG, "[WEBVIEW] shouldInterceptRequest(API 21 after):  url[" + request.getUrl() + "]");
        return this.assetLoader.shouldInterceptRequest(request.getUrl());
    }

    @Deprecated
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Logger.i(TAG, "[WEBVIEW] shouldOverrideUrlLoading() API 23 below: " + url);

        if (url.startsWith("http://") || url.startsWith("https://")) {
            WebViewHelper.loadUrl(view, url);
            return true;
        }
        return intentProcessing(view, url);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Logger.i(TAG, "[WEBVIEW] shouldOverrideUrlLoading() API 24 after: " + request.getUrl());

        String url = Uri.decode(request.getUrl().toString());

        if (url.startsWith("http://") || url.startsWith("https://")) {
            if (request.isRedirect()) {
                WebViewHelper.loadUrl(view, url);
                return true;
            }
            return false;
        }

        intentProcessing(view, request.getUrl().toString());
        return true;
    }

    private boolean intentProcessing(WebView view, String urlString) {
        String url = Uri.decode(urlString);

        if (url.startsWith("intent:")) {
            try {
                IntentUtil.scheme(view.getContext(), url);
                return true;
            } catch (URISyntaxException | ActivityNotFoundException e) {
                Logger.e(TAG, e);
            }
        }

        try {
            IntentUtil.view(view.getContext(), Uri.parse(url));
            return true;
        } catch (ActivityNotFoundException e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Logger.i(TAG, "[WEBVIEW] onPageStarted(): " + url);
        showProgress(view);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Logger.i(TAG, "[WEBVIEW] onPageFinished(): " + url);
        hideProgress(view);
        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, final String url) {
        Logger.d(TAG, "[WEBVIEW] onLoadResource():  url[" + url + "]");
        super.onLoadResource(view, url);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Logger.e(TAG, "[WEBVIEW] onReceivedSslError(): url[" + view.getUrl() + "],  handler[" + handler + "],  error[" + error + "]");

        if (SslError.SSL_NOTYETVALID == error.getPrimaryError()) {
            handler.proceed();
        } else if (SslError.SSL_EXPIRED == error.getPrimaryError()) {
            handler.proceed();
        } else if (SslError.SSL_IDMISMATCH == error.getPrimaryError()) {
            handler.proceed();
        } else if (SslError.SSL_UNTRUSTED == error.getPrimaryError()) {
            handler.proceed();
        } else if (SslError.SSL_DATE_INVALID == error.getPrimaryError()) {
            handler.proceed();
        } else if (SslError.SSL_INVALID == error.getPrimaryError()) {
            handler.proceed();
        } else {
            handler.proceed();
        }

        //super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        StringBuilder buff = new StringBuilder();
        buff.append("\n  encoding[").append(errorResponse.getEncoding()).append("]  ");
        buff.append("\n  mimeType[").append(errorResponse.getMimeType()).append("]  ");

        String url = request.getUrl().toString();

        buff.append("\n  method[").append(request.getMethod()).append("]  ");
        buff.append("\n  statusCode[").append(errorResponse.getStatusCode()).append("]  ");
        buff.append("\n  responseHeaders[").append(errorResponse.getResponseHeaders()).append("]  ");
        buff.append("\n  reasonPhrase[").append(errorResponse.getReasonPhrase()).append("]  ");

        Logger.e(TAG, "onReceivedHttpError : url[" + url + "],  errorResponse[" + buff.toString() + "]");
        super.onReceivedHttpError(view, request, errorResponse);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Logger.e(TAG, "[WEBVIEW] onReceivedError(VERSION=M): url[" + view.getUrl() + "],  errorCode[" + error.getErrorCode() + "],  description[" + error.getDescription() + "]");

        int errorCode = error.getErrorCode();
        String description = error.getDescription().toString();
        boolean forwardErrorPage = ERROR_BAD_URL == errorCode || ERROR_FILE == errorCode;
        onReceivedError(view, errorCode, description, request.getUrl().toString(), forwardErrorPage);
    }

    private void onReceivedError(WebView view, int errorCode, String description, String failingUrl, boolean forwardErrorPage) {
        if (WebViewClient.ERROR_UNSUPPORTED_SCHEME == errorCode) {
            if ("about:blank".equals(failingUrl)) {
                return;
            }
        }
        if (WebViewClient.ERROR_TOO_MANY_REQUESTS == errorCode) {
            return;
        }
        if (WebViewClient.ERROR_FAILED_SSL_HANDSHAKE == errorCode) {
            return;
        }
        if (WebViewClient.ERROR_HOST_LOOKUP == errorCode
                || WebViewClient.ERROR_TIMEOUT == errorCode
                || WebViewClient.ERROR_UNKNOWN == errorCode
        ) {
            final String[] except = new String[] {
                    ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tif", ".tiff", ".ico",
                    ".eot", ".ttf", ".otf", ".eot", ".woff", ".woff2",
                    ".pdf",
            };
            for (String str : except) {
                if (failingUrl.endsWith(str)) {
                    return;
                }
            }
        }
        if (forwardErrorPage) {
            String errorPageUrl = "file:///android_asset/docs/error/net_error.html";
            String qs = "errorCode=" + errorCode + "&description=" + description + "&failingUrl=" + failingUrl;
            WebViewHelper.loadUrl(view, errorPageUrl + "?" + qs);
        }
    }

    private void showProgress(WebView view) {
        try {
            if (!(view.getTag() instanceof JSONObject)) {
                return;
            }
            if (JSONHelper.getBoolean((JSONObject) view.getTag(), "showProgress", false)) {
                ((BaseActivity) view.getContext()).showProgress();
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void hideProgress(WebView view) {
        try {
            if (!(view.getTag() instanceof JSONObject)) {
                return;
            }
            JSONObject jsonObject = (JSONObject) view.getTag();
            if (JSONHelper.getBoolean(jsonObject, "showProgress", false)) {
                ((BaseActivity) view.getContext()).hideProgress();
                JSONHelper.put(jsonObject, "showProgress", false);
                view.setTag(jsonObject);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

}

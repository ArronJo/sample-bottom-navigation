package com.snc.zero.webview.client;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.snc.sample.bottom_navigation.ui.webview.helper.WebViewHelper;
import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.log.Logger;
import com.snc.zero.permission.RPermission;
import com.snc.zero.permission.RPermissionListener;
import com.snc.zero.util.StringUtil;
import com.snc.zero.webview.listener.CSFileChooserListener;

import java.util.ArrayList;
import java.util.List;

/**
 * WebChromeClient
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class CSWebChromeClient extends WebChromeClient {
    private static final String TAG = CSWebChromeClient.class.getSimpleName();

    private final Context context;
    private CSFileChooserListener fileChooserListener;

    // constructor
    public CSWebChromeClient(Context context) {
        this.context = context;
    }


    //++ [[START] File Chooser]
    public void setFileChooserListener(CSFileChooserListener listener) {
        this.fileChooserListener = listener;
    }

    // For Android 5.0+
    public boolean onShowFileChooser(final WebView webView, final ValueCallback<Uri[]> filePathCallback, final FileChooserParams fileChooserParams) {
        Logger.i(TAG, "[WEBVIEW] openFileChooser()  For Android 5.0+ \n:: filePathCallback[" + filePathCallback + "]  fileChooserParams[" + fileChooserParams + "]");

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        RPermission.with(this.context)
                .setPermissionListener(new RPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Logger.i(TAG, "[WEBVIEW] onPermissionGranted()");

                        String[] acceptType = fileChooserParams.getAcceptTypes();
                        if (null != fileChooserListener) {
                            fileChooserListener.onOpenFileChooserLollipop(webView, filePathCallback, acceptType);
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions, int status) {
                        Logger.e(TAG, "[WEBVIEW] onPermissionDenied()..." + deniedPermissions.toString());
                    }
                })
                .setPermissions(permissions)
                .check();
        return true;
    }
    //-- [[E N D] File Chooser]


    //++ [[START] Javascript Alert]
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Logger.i(TAG, "[WEBVIEW] onJsAlert(): url[" + view.getUrl() + "], message[" + message + "], JsResult[" + result + "]");

        String title = StringUtil.nvl(Uri.parse(url).getLastPathSegment(), "");
        DialogBuilder.with(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
                .show();
        return true;
    }
    //-- [[E N D] Javascript Alert]


    //++ [[START] Web Console Log]
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Logger.i(TAG, "[WEBVIEW] " + consoleMessage.messageLevel() + ":CONSOLE] \"" + consoleMessage.message() + "\", source: " + consoleMessage.sourceId() + " (" + consoleMessage.lineNumber() + ")");
        return true;    // remove chromium log
    }
    //-- [[E N D] Web Console Log]


    //++ [[START] Support Multiple Windows]
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        Logger.i(TAG, "[WEBVIEW] onCreateWindow():  view[" + view + "]  isDialog[" + isDialog + "]  isUserGesture[" + isUserGesture + "]  resultMsg[" + resultMsg + "]");

        WebView newWebView = WebViewHelper.newWebView(view.getContext());
        WebViewHelper.setupClient(newWebView);

        newWebView.setWebViewClient(new CSWebViewClient(view.getContext()) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.i(TAG, "[WEBVIEW] shouldOverrideUrlLoading : " + url);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                view.getContext().startActivity(intent);
                view.destroy();
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Logger.i(TAG, "[WEBVIEW] shouldOverrideUrlLoading :  WebResourceRequest ::" +
                        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? "  isRedirect[" + request.isRedirect() + "]" : "") +
                        "  Method[" + request.getMethod() + "]" +
                        "  Url[" + request.getUrl() + "]" +
                        "  hasGesture[" + request.hasGesture() + "]" +
                        "  isForMainFrame[" + request.isForMainFrame() + "]" +
                        "  RequestHeaders[" + request.getRequestHeaders() + "]");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(request.getUrl());
                view.getContext().startActivity(intent);
                view.destroy();
                return true;    //return super.shouldOverrideUrlLoading(view, request);
            }
        });

        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(newWebView);
        resultMsg.sendToTarget();
        return true;    //return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    @Override
    public void onCloseWindow(WebView window) {
        super.onCloseWindow(window);
        Logger.i(TAG, "[WEBVIEW] onCloseWindow()");
    }
    //-- [[E N D] Support Multiple Windows]

}

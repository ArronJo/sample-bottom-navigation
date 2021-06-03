package com.snc.sample.bottom_navigation.ui.webview.bridge;

import android.app.Activity;
import android.net.Uri;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.json.helper.JSONHelper;
import com.snc.zero.log.Logger;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * WebView Javascript Interface
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
@SuppressWarnings("ALL")
public class AndroidBridge {
    private static final String TAG = AndroidBridge.class.getSimpleName();

    private static final String SCHEME_JAVASCRIPT = "javascript:";

    private static final String SCHEME_BRIDGE = "native";

    private static final String HOST_COMMAND = "callNative";

    private final WebView webView;

    private static final Map<String, String> callbackFunctionNames = new HashMap<>();
    private static File extraOutput;

    public AndroidBridge(WebView webView) {
        this.webView = webView;
    }


    //////////////////////////////////////////////

    //++ [START] call Web --> Native

    // ex) "native://callNative?" + btoa(encodeURIComponent(JSON.stringify({ command:\"apiSample\", args{max:1,min:1}, callback:\"callbackNativeResponse\" })))
    @JavascriptInterface
    public boolean callNativeMethod(String urlString) {
        //Logger.i(TAG, "[WEBVIEW] callNativeMethod: " + urlString);
        try {
            return executeProcess(this.webView, parse(urlString));
        } catch (Exception e) {
            Logger.e(TAG, e);
            if (webView.getContext() instanceof Activity) {
                DialogBuilder.with(webView.getContext())
                        .setMessage(e.toString())
                        .show();
            }
        }
        return false;
    }

    private boolean executeProcess(final WebView webview, final JSONObject jsonObject) {
        final String command = JSONHelper.getString(jsonObject, "command", "");
        final JSONObject args = JSONHelper.getJSONObject(jsonObject, "args", new JSONObject());
        final String callback = JSONHelper.getString(jsonObject, "callback", "");

        Logger.i(TAG, "[WEBVIEW] callNativeMethod: executeProcess() :  command = " + command + ",  args = " + args + ",  callback = " + callback);
        Toast.makeText(webview.getContext(), "command = " + command + ",  args = " + args + ",  callback = " + callback, Toast.LENGTH_SHORT).show();

        try {
            if ("xxx".equals(command)) {
                return true;
            }

        } catch (Exception e) {
            DialogBuilder.with(webView.getContext())
                    .setMessage(e.toString())
                    .show();
            Logger.e(TAG, e);
        }
        return false;
    }

    private JSONObject parse(String urlString) throws IOException {
        Uri uri = Uri.parse(urlString);

        if (!SCHEME_BRIDGE.equals(uri.getScheme())) {
            throw new IOException("\"" + uri.getScheme() + "\" scheme is not supported.");
        }
        if (!HOST_COMMAND.equals(uri.getHost())) {
            throw new IOException("\"" + uri.getHost() + "\" host is not supported.");
        }

        String query = uri.getEncodedQuery();
        try {
            query = new String(Base64.decode(query, Base64.DEFAULT));
            query = URLDecoder.decode(query, "utf-8");
            return new JSONObject(query);
        } catch (Exception e) {
            throw new IOException("\"" + query + "\" is not JSONObject.");
        }
    }

    //-- [E N D] call Web --> Native


    //////////////////////////////////////////////

    //++ [START] call Native --> Web

    public static void callJSFunction(final WebView webView, String functionName, String... params) {
        String js = makeJavascript(functionName, params);
        Logger.i(TAG, "[WEBVIEW] callJSFunction() :  javascript = " + js);

        final StringBuilder buff = new StringBuilder();
        buff.append("(function() {\n");
        buff.append("  try {\n");
        buff.append("    ").append(js).append("\n");
        buff.append("  } catch(e) {\n");
        buff.append("    return '[JS Error] ' + e.message;\n");
        buff.append("  }\n");
        buff.append("})(window);");

        // Run On UIThread
        webView.post(() -> evaluateJavascript(webView, buff.toString()));
    }

    public static String makeJavascript(String functionName, String... params) {
        final StringBuilder buff = new StringBuilder();
        buff.append(functionName).append("(");

        for (int i = 0; i < params.length; i++) {
            Object param = params[i];

            // 데이터 설정
            if (null != param) {
                buff.append("'").append(param).append("'");
            } else {
                buff.append("''");
            }

            if (i < params.length - 1) {
                buff.append(", ");
            }
        }
        buff.append("); ");
        return buff.toString();
    }

    private static void evaluateJavascript(final WebView webView, final String javascriptString) {
        String jsString = javascriptString;
        if (jsString.startsWith(SCHEME_JAVASCRIPT)) {
            jsString = jsString.substring(SCHEME_JAVASCRIPT.length());
        }
        jsString = jsString.replaceAll("\t", "    ");
        webView.evaluateJavascript(jsString, value -> Logger.i(TAG, "[WEBVIEW] onReceiveValue: " + value));
    }

    //-- [E N D] call Native --> Web


    //////////////////////////////////////////////

    //++ [START] for JS Callback

    public static void setCallbackJSFunctionName(int requestCode, String functionName) {
        callbackFunctionNames.put(String.valueOf(requestCode), functionName);
    }

    public static String getCallbackJSFunctionName(int requestCode) {
        return callbackFunctionNames.remove(String.valueOf(requestCode));
    }

    public static File getExtraOutput(boolean pop) {
        if (pop) {
            File file = extraOutput;
            extraOutput = null;
            return file;
        }
        return extraOutput;
    }

    public static void setExtraOutput(File file) {
        extraOutput = file;
    }

    //-- [E N D] for JS Callback

    //////////////////////////////////////////////

}

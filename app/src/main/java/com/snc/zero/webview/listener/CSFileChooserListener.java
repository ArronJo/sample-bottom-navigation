package com.snc.zero.webview.listener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.snc.zero.dialog.DialogBuilder;
import com.snc.zero.log.Logger;
import com.snc.zero.util.DateTimeUtil;
import com.snc.zero.util.EnvUtil;
import com.snc.zero.util.StringUtil;
import com.snc.zero.util.UriUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.activity.ComponentActivity;
import androidx.activity.result.contract.ActivityResultContracts;

/**
 * WebView FileChooser Listener
 *
 * @author mcharima5@gmail.com
 * @since 2021
 */
public class CSFileChooserListener {
    private static final String TAG = CSFileChooserListener.class.getSimpleName();

    private final Context context;

    private ValueCallback<Uri[]> filePathCallbackLollipop;
    private final int IMAGE = 0;
    private final int AUDIO = 1;
    private final int VIDEO = 2;
    private Uri[] mediaURIs;


    /////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////

    public CSFileChooserListener(Context context) {
        this.context = context;
    }


    /////////////////////////////////////////////////
    // Open FileChooser
    /////////////////////////////////////////////////

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public void onOpenFileChooserLollipop(WebView webView, ValueCallback<Uri[]> filePathCallback, String[] acceptType) {
        this.filePathCallbackLollipop = filePathCallback;
        openIntentChooser(acceptType);
    }

    private void openIntentChooser(String[] acceptTypes) {
        String acceptType = "";
        for (String type : acceptTypes) {
            if (StringUtil.isEmpty(type)) {
                continue;
            }
            if (!type.startsWith("image/")
                    && !type.startsWith("audio/")
                    && !type.startsWith("video/")
                    && !type.startsWith("application/")) {
                continue;
            }

            if (StringUtil.isEmpty(acceptType)) {
                acceptType += type;
            } else {
                acceptType += "," + type;
            }
        }

        openIntentChooser(acceptType);
    }

    private static final String ALL_TYPE = "image/*|audio/*|video/*";
    private void openIntentChooser(String acceptType) {
        String type = acceptType;
        if (type.isEmpty() || "*/*".equalsIgnoreCase(acceptType)) {
            type = ALL_TYPE;
        }

        try {
            mediaURIs = new Uri[3];

            List<Intent> intentList = new ArrayList<>();

            String fileName = DateTimeUtil.formatDate(new Date(), "yyyyMMdd_HHmmss");

            if (type.contains("image/")) {
                mediaURIs[IMAGE] = UriUtil.fromFile(context, new File(EnvUtil.getMediaDir(context, "image"), fileName + ".jpg"));

                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaURIs[IMAGE]);
                intentList.add(intent);
            }
            if (type.contains("audio/")) {
                mediaURIs[AUDIO] = UriUtil.fromFile(context, new File(EnvUtil.getMediaDir(context, "audio"), fileName + ".m4a"));

                final Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaURIs[AUDIO]);
                intentList.add(intent);
            }
            if (type.contains("video/")) {
                mediaURIs[VIDEO] =  UriUtil.fromFile(context, new File(EnvUtil.getMediaDir(context, "video"), fileName + ".mp4"));

                final Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaURIs[VIDEO]);
                intentList.add(intent);
            }

            // Intent Chooser
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            if (ALL_TYPE.equals(type)) {
                intent.setType("*/*");
            } else {
                intent.setType(type);
            }

            Intent chooserIntent = Intent.createChooser(intent, "Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[] { }));

            ((ComponentActivity) context).registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> onActivityResultFileChooserLollipop(-1, result.getResultCode(), result.getData())
            ).launch(intent);

        } catch (Exception e) {
            Logger.e(TAG, e);

            DialogBuilder.with(context)
                    .setMessage(e.toString())
                    .show();
        }
    }


    /////////////////////////////////////////////////
    // onActivityResult
    /////////////////////////////////////////////////

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onActivityResultFileChooserLollipop(int requestCode, int resultCode, Intent data) {
        Logger.i(TAG, "[WEBVIEW] onActivityResultFileChooserLollipop(): requestCode[" + requestCode + "]  resultCode[" + resultCode + "] data[" + data + "]");

        if (null == filePathCallbackLollipop) {
            Logger.i(TAG, "[WEBVIEW] onActivityResultLollipop(): filePathCallbackLollipop is null !!!");
            return;
        }

        if (Activity.RESULT_OK != resultCode) {
            filePathCallbackLollipop.onReceiveValue(null);
            filePathCallbackLollipop = null;
            return;
        }

        try {
            if (null != data) {
                filePathCallbackLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            } else {
                List<Uri> results = new ArrayList<>();

                if (null != mediaURIs) {
                    if (null != mediaURIs[IMAGE]) {
                        results.add(mediaURIs[IMAGE]);
                    }
                    if (null != mediaURIs[AUDIO]) {
                        results.add(mediaURIs[AUDIO]);
                    }
                    if (null != mediaURIs[VIDEO]) {
                        results.add(mediaURIs[VIDEO]);
                    }
                }

                filePathCallbackLollipop.onReceiveValue(results.toArray(new Uri[]{}));
            }

        } catch (Exception e) {
            Logger.e(TAG, e);
        } finally {
            filePathCallbackLollipop = null;
            mediaURIs = null;
        }
    }

}

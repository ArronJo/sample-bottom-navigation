package com.snc.zero.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.net.URISyntaxException;

/**
 * Intent Utilities
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class IntentUtil {

    public static String getStringExtra(Intent intent, String key, String defaultValue) {
        if (!intent.hasExtra(key)) {
            return defaultValue;
        }
        return intent.getStringExtra(key);
    }

    public static void view(Context context, Uri uri) throws ActivityNotFoundException {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void scheme(Context context, String urlString) throws URISyntaxException {
        Intent intent = Intent.parseUri(urlString, Intent.URI_INTENT_SCHEME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
            marketLaunch.setData(Uri.parse("market://details?id=" + intent.getPackage()));
            context.startActivity(marketLaunch);
        }
    }
}

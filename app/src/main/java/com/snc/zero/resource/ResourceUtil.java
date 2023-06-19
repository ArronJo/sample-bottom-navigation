package com.snc.zero.resource;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;

import com.snc.zero.log.Logger;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

/**
 * Resource Utilities
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class ResourceUtil {
    private static final String TAG = ResourceUtil.class.getSimpleName();

    public static ColorStateList getColorStateList(Context context, int resId) {
        return AppCompatResources.getColorStateList(context, resId);
    }

    public static int getColor(Context context, int resId) {
        return ContextCompat.getColor(context, resId);
    }

    public static String getString(Context context, int resId) {
        try {
            return context.getString(resId);
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
        return "";
    }

}

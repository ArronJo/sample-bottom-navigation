package com.snc.zero.util;

import android.text.TextUtils;

/**
 * String Utilities
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class StringUtil {

    public static String nvl(String str, String defaultValue) {
        return !isEmpty(str) ? str : defaultValue;
    }

    public static boolean isEmpty(String obj) {
        return TextUtils.isEmpty(obj);
    }

}

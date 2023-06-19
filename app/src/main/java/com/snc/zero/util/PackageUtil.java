package com.snc.zero.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Package Utilities
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class PackageUtil {

    public static String getPackageDisplayVersion(Context context) throws PackageManager.NameNotFoundException {
        StringBuilder version = new StringBuilder();
        String verName = getPackageVersionName(context);
        version.append("ver.").append(verName);
        int rev = getPackageVersionCode(context);
        version.append("  (rev.").append(rev).append(")");
        return version.toString();
    }

    public static String getPackageVersionName(Context context) throws PackageManager.NameNotFoundException {
        return getPackageInfo(context).versionName;
    }

    public static int getPackageVersionCode(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo info = getPackageInfo(context);
        int packageVersionCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageVersionCode = (int) info.getLongVersionCode();
        } else {
            packageVersionCode = info.versionCode;
        }
        return packageVersionCode;
    }

    public static String getPackageName(Context context) throws PackageManager.NameNotFoundException {
        return getPackageInfo(context).packageName;
    }

    private static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
    }

}

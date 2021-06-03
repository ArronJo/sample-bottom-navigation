package com.snc.zero.permission;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.gun0912.tedpermission.TedPermission;
import com.snc.zero.log.Logger;
import com.snc.zero.resource.ResourceUtil;
import com.snc.zero.util.StringUtil;

import java.util.List;

import androidx.core.app.ActivityCompat;

/**
 * Runtime Permission
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class RPermission {
    private static final String TAG = RPermission.class.getSimpleName();

    public static final int SHOW_REQUEST_RATIONALE = 0;
    public static final int ALREADY_DENIED_PERMISSION = 1;

    public static RPermission with(Context context) {
        return new RPermission(context);
    }

    private final Context context;
    private String rationalMessage;
    private String[] permissions;
    private RPermissionListener listener;
    private final android.os.Handler mHandler = new Handler(Looper.getMainLooper());

    public RPermission(Context context) {
        this.context = context;
    }

    public RPermission setRationalMessage(String rationalMessage) {
        this.rationalMessage = rationalMessage;
        return this;
    }

    public RPermission setRationalMessage(int resId) {
        this.rationalMessage = ResourceUtil.getString(this.context, resId);
        return this;
    }

    public RPermission setPermissionListener(RPermissionListener listener) {
        this.listener = listener;
        return this;
    }

    public RPermission setPermissions(List<String> permissions) {
        return setPermissions(permissions.toArray(new String[] {}));
    }

    public RPermission setPermissions(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public void check() {
        TedPermission.Builder builder = TedPermission.with(context);
        if (!StringUtil.isEmpty(rationalMessage)) {
            builder.setRationaleMessage(rationalMessage);
        }
        builder.setPermissionListener(new com.gun0912.tedpermission.PermissionListener() {

            @Override
            public void onPermissionGranted() {
                mHandler.post(() -> {
                    if (null != listener) {
                        listener.onPermissionGranted();
                    }
                });
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                mHandler.post(() -> {
                    if (null != listener) {
                        if (!shouldShowRequestPermissionRationale(deniedPermissions)) {
                            Logger.e(TAG, "거부한 적이 있는 권한 거절");
                            listener.onPermissionDenied(deniedPermissions, ALREADY_DENIED_PERMISSION);
                        } else {
                            Logger.e(TAG, "처음 거부하는 권한 거절");
                            listener.onPermissionDenied(deniedPermissions, SHOW_REQUEST_RATIONALE);
                        }
                    }
                });
            }
        });
        builder.setPermissions(permissions);
        builder.check();
    }

    public boolean shouldShowRequestPermissionRationale(List<String> needPermissions) {
        if (null == needPermissions) {
            return false;
        }

        for (String permission : needPermissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) this.context, permission)) {
                return false;
            }
        }
        return true;
    }

}

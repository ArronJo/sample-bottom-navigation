package com.snc.zero.permission;

import java.util.List;

/**
 * Runtime Permission Listener
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public interface RPermissionListener {

    void onPermissionGranted();

    void onPermissionDenied(List<String> deniedPermissions, int status);

}
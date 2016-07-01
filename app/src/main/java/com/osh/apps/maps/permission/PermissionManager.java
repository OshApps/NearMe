package com.osh.apps.maps.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;


public class PermissionManager
{

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(Fragment fragment, String permission, int requestCode )
    {
    boolean hasPermission=hasPermission(fragment.getActivity(), permission);

    if(!hasPermission)
        {
        fragment.requestPermissions(new String[]{permission}, requestCode);
        }

    return hasPermission;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(Activity activity, String permission, int requestCode )
    {
    boolean hasPermission=hasPermission(activity, permission);

    if(!hasPermission)
        {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }

    return hasPermission;
    }


    private static boolean hasPermission(Activity activity, String permission )
    {
    return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || ( ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED);
    }

}

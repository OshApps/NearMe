package com.osh.apps.maps.permission;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;


public class PermissionManager
{

    public static boolean checkPermission(Fragment fragment, String permission, int requestCode )
    {
    boolean hasPermission=true;

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
        if(ContextCompat.checkSelfPermission(fragment.getActivity(), permission) != PackageManager.PERMISSION_GRANTED)
            {
            fragment.requestPermissions(new String[]{permission}, requestCode);
            hasPermission=false;
            }
        }

    return hasPermission;
    }
}

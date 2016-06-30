package com.osh.apps.maps.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;

import com.osh.apps.maps.R;
import com.osh.apps.maps.dialog.SimpleAlertDialog;
import com.osh.apps.maps.location.LocationTrackerManager;
import com.osh.apps.maps.permission.PermissionManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


/**
 * Created by oshri-n on 26/06/2016.
 */
public abstract class BaseActivity extends AppCompatActivity implements LocationTrackerManager.OnLocationChangedListener
{
private static final int LOCATION_REQUEST_CODE=0;

private LocationTrackerManager locationTrackerManager;
private Runnable lastAction;


abstract protected void onCreate();
abstract protected void onCreateView();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    onCreate();

    createLocationTrackerManager(null);

    onCreateView();
    }


    public LocationTrackerManager getLocationTrackerManager(Runnable action)
    {

    if(locationTrackerManager==null)
        {
        createLocationTrackerManager(action);
        }

    return locationTrackerManager;
    }


    private void createLocationTrackerManager(Runnable action)
    {
    lastAction=action;

    if(PermissionManager.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE))
        {
        createLocationTrackerManager();
        }
    }


    @RequiresPermission(anyOf = {ACCESS_FINE_LOCATION})
    private void createLocationTrackerManager()
    {
    locationTrackerManager=LocationTrackerManager.getInstance(this);

    if(lastAction!=null)
        {
        lastAction.run();
        }

    lastAction=null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    switch(requestCode)
        {
        case LOCATION_REQUEST_CODE:

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
            createLocationTrackerManager();
            }else
                {
                SimpleAlertDialog.createAlertDialog(this, getString(R.string.error), getString(R.string.no_location_access), getString(R.string.ok), null, null);
                }

        break;
        }
    }


    @Override
    protected void onResume()
    {
    super.onResume();

    if(locationTrackerManager != null)
        {
        locationTrackerManager.registerLocationUpdates(this);
        onLocationChanged(locationTrackerManager.getCurrentLocation());
        }
    }


    @Override
    protected void onPause()
    {
    super.onPause();

    if(locationTrackerManager != null)
        {
        locationTrackerManager.removeLocationUpdates();
        }
    }


    public Location getLocation()
    {
    Location location=null;

    if(locationTrackerManager !=null)
        {
        location=locationTrackerManager.getCurrentLocation();
        }

    return location;
    }

}

package com.osh.apps.maps.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


/**
 * Created by oshri-n on 13/06/2016.
 */
public class LocationRequest implements LocationListener
{
private static final int GPS_TIMEOUT=1000;

private LocationRequestListener locationRequestListener;
private Runnable networkLocationRequest;
private LocationManager locationManager;
private Handler handler;
private String keyword;


    public LocationRequest(LocationManager locationManager, LocationRequestListener locationRequestListener)
    {
    this.locationManager=locationManager;
    this.locationRequestListener=locationRequestListener;

    handler=new Handler();

    networkLocationRequest=new Runnable()
        {
            @Override
            public void run()
            {
            requestLocationListener(LocationManager.NETWORK_PROVIDER);
            }
        };
    }


    public void setKeyword(String keyword)
    {
    this.keyword=keyword;
    }


    @RequiresPermission(anyOf = {ACCESS_FINE_LOCATION})
    public void prepareRequest()
    {
    handler.removeCallbacks(networkLocationRequest);
    handler.postDelayed(networkLocationRequest, GPS_TIMEOUT);

    requestLocationListener(LocationManager.GPS_PROVIDER);
    }


    public void requestLocationListener(String provider)
    {
    locationManager.removeUpdates(this);

    if(locationManager.getAllProviders().contains(provider))
        {
        locationManager.requestSingleUpdate(provider, this, null );
        }
    }


    @Override
    public void onLocationChanged(Location location)
    {
    handler.removeCallbacks(networkLocationRequest);

    Log.d("onLocationChanged",location.getProvider());

    locationRequestListener.onRequestReady(keyword, location.getLatitude(), location.getLongitude());
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }


    @Override
    public void onProviderEnabled(String provider)
    {

    }


    @Override
    public void onProviderDisabled(String provider)
    {

    }


    public interface LocationRequestListener
    {
        void onRequestReady(String keyword, double lat, double lng);
    }

}

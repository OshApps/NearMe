package com.osh.apps.nearme.fragment;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.osh.apps.nearme.R;
import com.osh.apps.nearme.permission.PermissionManager;

import java.security.Permissions;


/**
 * Created by oshri-n on 29/06/2016.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback
{
private static final int TITLE_RES=R.string.title_tab_map;

private static final int DEFAULT_ZOOM=18;

private static final String ARG_PLACE_NAME="placeName";
private static final String ARG_PLACE_LAT="placeLat";
private static final String ARG_PLACE_LNG="placeLng";

private FloatingActionButton myLocationButton;
private LatLng placeLocation,myLocation;
private GoogleMap googleMap;
private String placeName;
private MapView mapView;


    public MapFragment()
    {

    }


    public static MapFragment newInstance(String placeName, double placeLat, double placeLng)
    {
    MapFragment fragment;
    Bundle args;

    args=new Bundle();

    args.putString(ARG_PLACE_NAME, placeName);
    args.putDouble(ARG_PLACE_LAT, placeLat);
    args.putDouble(ARG_PLACE_LNG, placeLng);

    fragment=new MapFragment();
    fragment.setArguments(args);

    return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    Bundle args;

    args=getArguments();

    placeName=args.getString(ARG_PLACE_NAME);

    placeLocation=new LatLng(args.getDouble(ARG_PLACE_LAT), args.getDouble(ARG_PLACE_LNG));

    googleMap=null;
    myLocation=null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
    View view=inflater.inflate(R.layout.fragment_map, container, false);

    mapView = (MapView) view.findViewById(R.id.mapview);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

    myLocationButton=(FloatingActionButton) view.findViewById(R.id.ib_my_location);
    myLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            moveUserMarker();
            }
        });


    if(myLocation==null)
        {
        myLocationButton.setVisibility(View.GONE);
        }

    return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
    this.googleMap=googleMap;

    googleMap.addMarker(new MarkerOptions().position(placeLocation).title(placeName));//.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place_marker)));
    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, DEFAULT_ZOOM));

    if(PermissionManager.hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION))
        {
        googleMap.setMyLocationEnabled(true);
        }

    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }


    private void moveUserMarker()
    {

    if(myLocation!=null && googleMap != null)
        {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, DEFAULT_ZOOM));
        }
    }


    @Override
    public void onLocationChanged(Location location)
    {

    if(location != null)
        {
        myLocation=new LatLng(location.getLatitude(), location.getLongitude());

        if(isCreated())
            {
            myLocationButton.setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    public void onResume()
    {
    super.onResume();
    mapView.onResume();
    }


    @Override
    public void onPause()
    {
    super.onPause();
    mapView.onPause();
    }


    @Override
    public void onDestroy()
    {
    super.onDestroy();
    mapView.onDestroy();
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onLowMemory()
    {
    super.onLowMemory();
    mapView.onLowMemory();
    }


    @Override
    public int getTitleRes()
    {
    return TITLE_RES;
    }

}

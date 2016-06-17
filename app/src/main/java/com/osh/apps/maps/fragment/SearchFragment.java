package com.osh.apps.maps.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.osh.apps.maps.activity.PlaceDetailsActivity;
import com.osh.apps.maps.R;
import com.osh.apps.maps.location.LocationRequest;
import com.osh.apps.maps.adapter.PlaceAdapter;
import com.osh.apps.maps.app.AppData;
import com.osh.apps.maps.database.DatabaseManager;
import com.osh.apps.maps.permission.PermissionManager;
import com.osh.apps.maps.place.Place;
import com.osh.apps.maps.service.SearchService;
import com.osh.apps.maps.widget.recyclerview.CustomRecyclerView;


public class SearchFragment extends TabFragment implements LocationRequest.LocationRequestListener, CustomRecyclerView.OnItemClickListener
{
private static final int TITLE_RES=R.string.search_tab;

private static final int LOCATION_REQUEST_CODE=0;

private CustomRecyclerView recyclerView;
private DatabaseManager databaseManager;
private SearchReceiver searchReceiver;
private LocationRequest locationRequest;
private PlaceAdapter adapter;
private ProgressBar loading;
private TextView msg;


    public SearchFragment()
    {
    // Required empty public constructor
    }


    public static SearchFragment newInstance()
    {
    SearchFragment fragment=new SearchFragment();
    return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    databaseManager=DatabaseManager.getInstance(getContext());

    adapter=new PlaceAdapter(getContext(), R.layout.rv_place_item);
    adapter.setPlaces(databaseManager.getLastSearch());

    searchReceiver=new SearchReceiver();

    locationRequest=new LocationRequest((LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE), this);

    LocalBroadcastManager.getInstance(getContext()).registerReceiver(searchReceiver,new IntentFilter(SearchService.ACTION_SEARCH));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    // Inflate the layout for this fragment
    View view=inflater.inflate(R.layout.fragment_search, container, false);

    recyclerView=(CustomRecyclerView) view.findViewById(R.id.RecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);
    recyclerView.setOnItemClickListener(this);

    loading=(ProgressBar) view.findViewById(R.id.pb_loading);

    msg=(TextView) view.findViewById(R.id.tv_message);

    return view;
    }


    @Override
    public void onDestroy()
    {
    super.onDestroy();
    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(searchReceiver);
    }


    public void onSearch(String keyword)
    {
    adapter.setPlaces(null);

    loading.setVisibility(View.VISIBLE);
    msg.setVisibility(View.GONE);

    locationRequest.setKeyword(keyword);

    if(PermissionManager.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE))
        {
        locationRequest.prepareRequest();
        }
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
            locationRequest.prepareRequest();
            }else
                {
                msg.setText(getString(R.string.no_location_access));
                msg.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                }

        break;
        }

    }


    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int position, int viewType)
    {
    Place place=adapter.getItem(position);

    PlaceDetailsActivity.openActivity(getContext(), place.getName(), place.getId(), AppData.Place.SEARCH_TYPE);
    }


    @Override
    public void onRequestReady(String keyword, double lat, double lng)
    {
    SearchService.startActionSearch(getContext(), keyword, lat, lng);
    }


    @Override
    public int getTitleRes()
    {
    return TITLE_RES;
    }


    private class SearchReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
        String message;
        int status;

        message=null;

        status=intent.getIntExtra(SearchService.EXTRA_STATUS, SearchService.STATUS_ERROR);

        switch(status)
            {
            case SearchService.STATUS_OK:
            adapter.setPlaces(databaseManager.getLastSearch());
            break;

            case SearchService.STATUS_ZERO_RESULTS:
            message=getString(R.string.no_results);
            break;

            case SearchService.STATUS_ERROR:
            message=getString(R.string.search_failed);
            break;
            }

        if(message!=null)
            {
            msg.setText(message);
            msg.setVisibility(View.VISIBLE);
            }

        loading.setVisibility(View.GONE);
        }
    }

}
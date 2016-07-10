package com.osh.apps.nearme.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.osh.apps.nearme.menu.PlaceMenu;
import com.osh.apps.nearme.R;
import com.osh.apps.nearme.activity.callback.PlaceDetailsCallback;
import com.osh.apps.nearme.activity.callback.PlaceListCallback;
import com.osh.apps.nearme.adapter.FragmentsAdapter;
import com.osh.apps.nearme.app.AppData;
import com.osh.apps.nearme.database.DatabaseManager;
import com.osh.apps.nearme.dialog.SimpleAlertDialog;
import com.osh.apps.nearme.fragment.DetailsFragment;
import com.osh.apps.nearme.fragment.FavouritesFragment;
import com.osh.apps.nearme.fragment.MapFragment;
import com.osh.apps.nearme.fragment.SearchFragment;
import com.osh.apps.nearme.location.LocationTrackerManager;
import com.osh.apps.nearme.place.Place;

import java.util.List;


public class HomeActivity extends BaseActivity implements PlaceListCallback,PlaceDetailsCallback, PlaceMenu.Callback
{
private static final int SETTINGS_REQUEST=1;

private ConnectivityManager connectivityManager;
private FavouritesFragment favouritesFragment;
private FragmentsAdapter fragmentsAdapter;
private FragmentManager fragmentManager;
private DetailsFragment detailsFragment;
private DatabaseManager databaseManager;
private SearchFragment searchFragment;
private int searchFragmentPosition;
private MapFragment mapFragment;
private SearchView searchView;
private PlaceMenu placeMenu;
private ViewPager viewPager;
private boolean isLandscape;
private long lastPlaceId;


    @Override
    protected void onCreate()
    {
    lastPlaceId=AppData.NULL_DATA;

    placeMenu=new PlaceMenu(this);

    databaseManager=DatabaseManager.getInstance(this);

    isLandscape=getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

    connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    @Override
    protected void onCreateFragments()
    {
    List<Fragment> fragments;

    fragmentManager=getSupportFragmentManager();

    fragments=fragmentManager.getFragments();

    if(fragments != null)
        {

        for(Fragment fragment: fragments)
            {

            if(fragment instanceof SearchFragment)
                {
                searchFragment=(SearchFragment) fragment;
                continue;
                }

            if(fragment instanceof FavouritesFragment)
                {
                favouritesFragment=(FavouritesFragment) fragment;
                continue;
                }

            if(isLandscape)
                {
                if(fragment instanceof MapFragment)
                    {
                    mapFragment=(MapFragment) fragment;
                    continue;
                    }

                if(fragment instanceof DetailsFragment)
                    {
                    detailsFragment=(DetailsFragment) fragment;
                    continue;
                    }
                }

            }
        }

    if(searchFragment == null)
        {
        searchFragment=SearchFragment.newInstance();
        }

    if(favouritesFragment == null)
        {
        favouritesFragment=FavouritesFragment.newInstance();
        }

    if(isLandscape && mapFragment==null)
        {
        mapFragment= MapFragment.newInstance();
        }

    fragmentsAdapter=new FragmentsAdapter( this ,fragmentManager , searchFragment, favouritesFragment);

    searchFragmentPosition=fragmentsAdapter.getItemPosition(searchFragment);
    }


    @Override
    protected void onCreateView()
    {
    TabLayout tabLayout;

    setContentView(R.layout.activity_home);

    Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    viewPager=(ViewPager) findViewById(R.id.ViewPager);
    viewPager.setAdapter(fragmentsAdapter);

    if(isLandscape)
        {

        fragmentManager.beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }else
            {
            FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                    onSearch(null);
                    }
                });
            }

    tabLayout = (TabLayout) findViewById(R.id.tl_tabs);
    tabLayout.setupWithViewPager(viewPager);

    ViewCompat.setLayoutDirection(tabLayout, ViewCompat.LAYOUT_DIRECTION_LTR);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    MenuItem search,searchNearMe;

    getMenuInflater().inflate( R.menu.menu_home_activity, menu);

    search=menu.findItem( R.id.m_search);
    search.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

    searchView = (SearchView) search.getActionView();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
            hideKeyboard();

            onSearch(query);
            return true;
            }


            @Override
            public boolean onQueryTextChange(String s)
            {
            return false;
            }

        });

    searchNearMe=menu.findItem(R.id.m_search_nearme);

    if(isLandscape)
        {
        searchNearMe.setVisible(true);
        searchNearMe.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

    return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    Intent intent;
    boolean isActionDone=true;

    switch(item.getItemId())
        {
        case R.id.m_clear_history:
        databaseManager.deleteLastSearch();
        searchFragment.clear();
        break;

        case R.id.m_setting:
        intent=new Intent(this, SettingsActivity.class);
        startActivityForResult(intent,SETTINGS_REQUEST);
        break;

        case R.id.m_search_nearme:
        onSearch(null);
        break;

        default:
        isActionDone=false;
        }

    return isActionDone;
    }


    @Override
    public void onBackPressed()
    {
    super.onBackPressed();

    if(detailsFragment!=null)
        {
        detailsFragment=null;

        mapFragment.removePlaceMarker();
        }
    }


    @Override
    protected void onResume()
    {
    super.onResume();

    if(lastPlaceId != AppData.NULL_DATA)
        {
        onPlaceChanged(lastPlaceId);

        lastPlaceId=AppData.NULL_DATA;
        }
    }


    private void onPlaceChanged(long placeId)
    {
    favouritesFragment.onPlaceChanged(placeId);
    searchFragment.onPlaceChanged(placeId);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    super.onActivityResult(requestCode, resultCode, data);

    boolean isFavouritesRemoved,isDistanceTypeChanged;

    if (resultCode == RESULT_OK)
        {

        if(requestCode == SETTINGS_REQUEST)
            {
            isFavouritesRemoved=data.getBooleanExtra(SettingsActivity.KEY_FAVOURITES_REMOVED, false);
            isDistanceTypeChanged=data.getBooleanExtra(SettingsActivity.KEY_DISTANCE_TYPE_CHANGED, false);

            if(isFavouritesRemoved)
                {
                searchFragment.onFavouritesRemoved();
                favouritesFragment.onFavouritesRemoved();

                }else if(isDistanceTypeChanged)
                    {
                    searchFragment.refresh();
                    favouritesFragment.refresh();
                    }
            }
        }

    }


    public void onSearch(final String keyword)
    {
    LocationTrackerManager locationTrackerManager;
    Location location;

    locationTrackerManager=getLocationTrackerManager(new Runnable()
                {
                    @Override
                    public void run()
                    {
                    onSearch(keyword);
                    }
                });

    if(locationTrackerManager != null) //has permissions
        {

        if(locationTrackerManager.isLocationEnabled())
            {
            location=locationTrackerManager.getCurrentLocation();

            if(location !=null)
                {

                if(isOnline())
                    {
                    searchFragment.onSearch(keyword, location.getLatitude(), location.getLongitude());
                    }else
                        {
                        SimpleAlertDialog.createAlertDialog(this, getString(R.string.network_connection), getString(R.string.network_connection_msg), getString(R.string.settings), getString(R.string.cancel), new SimpleAlertDialog.AlertDialogListener()
                            {
                                @Override
                                public void onPositive(DialogInterface dialog)
                                {
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                                }


                                @Override
                                public void onNegative(DialogInterface dialog)
                                {

                                }
                            });

                        Log.d("HA-onSearch","network is not available");
                        }

                }else
                    {
                    searchFragment.onLocationNotFound();
                    Log.d("HA-onSearch","failed to find your location");
                    }
            }else
                {
                SimpleAlertDialog.createAlertDialog(this, getString(R.string.enable_location), getString(R.string.enable_location_msg), getString(R.string.settings), getString(R.string.cancel), new SimpleAlertDialog.AlertDialogListener()
                    {
                        @Override
                        public void onPositive(DialogInterface dialog)
                        {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }


                        @Override
                        public void onNegative(DialogInterface dialog)
                        {

                        }
                    });

                Log.d("HA-onSearch","location is not enable");
                }
        }else
            {
            Log.d("HA-onSearch","no have Permissions");
            }

    viewPager.setCurrentItem(searchFragmentPosition, true);
    }


    public boolean isOnline()
    {
    NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

    return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void onLocationChanged(Location location)
    {
    searchFragment.onLocationChanged(location);
    favouritesFragment.onLocationChanged(location);

    if(isLandscape)
        {
        mapFragment.onLocationChanged(location);

        if(detailsFragment != null)
            {
            detailsFragment.onLocationChanged(location);
            }
        }
    }


    private void hideKeyboard()
    {
    View view = getCurrentFocus();

    if (view != null)
        {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public Location getCurrentLocation()
    {
    return getLocation();
    }


    @Override
    public void onClickPlace(Place place)
    {

    if(isLandscape)
        {
        detailsFragment=DetailsFragment.newInstance(place.getId());

        fragmentManager.beginTransaction()
                        .replace(R.id.container,detailsFragment)
                        .addToBackStack("details")
                        .commit();

        mapFragment.setPlaceMarker(place.getId());

        placeMenu.startActionMode(place);
        }else
            {
            lastPlaceId=place.getId();

            PlaceDetailsActivity.openActivity(this, lastPlaceId);
            }
    }


    @Override
    public void onRemoveFavouritePlace(Fragment fragment, long placeId)
    {
    if(fragment == favouritesFragment)
        {
        searchFragment.onFavouritePlaceRemoved(placeId);

        }else if(fragment == searchFragment)
            {
            favouritesFragment.onFavouritePlaceRemoved(placeId);
            }
    }


    @Override
    public void onAddFavouritePlace(Place place)
    {
    favouritesFragment.onFavouritePlaceAdded(place);
    }


    @Override
    public void onClickDetail()
    {
    if(mapFragment != null)
        {
        mapFragment.animatePlaceMarker();
        }
    }


    @Override
    public void onStartPlaceActionMode(ActionMode.Callback callback)
    {
    startSupportActionMode(callback);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }


    @Override
    public void onFavouriteToggleChanged(long placeId, boolean isFavouritePlace)
    {
    databaseManager.updatePlace(placeId, isFavouritePlace);

    onPlaceChanged(placeId);
    }


    @Override
    public void onShareSelected(Place place)
    {
    AppData.sharePlace(this, place);
    }


    @Override
    public void onActionModeClosed()
    {
    onBackPressed();
    }
}

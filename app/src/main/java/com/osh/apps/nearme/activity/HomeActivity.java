package com.osh.apps.nearme.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.osh.apps.nearme.R;
import com.osh.apps.nearme.activity.callback.HomeActivityCallback;
import com.osh.apps.nearme.adapter.FragmentsAdapter;
import com.osh.apps.nearme.app.AppData;
import com.osh.apps.nearme.dialog.SimpleAlertDialog;
import com.osh.apps.nearme.fragment.FavouritesFragment;
import com.osh.apps.nearme.fragment.SearchFragment;
import com.osh.apps.nearme.location.LocationTrackerManager;
import com.osh.apps.nearme.place.Place;

import java.util.List;
import java.util.Locale;


public class HomeActivity extends BaseActivity implements HomeActivityCallback
{
private static final int SETTINGS_REQUEST=1;

private ConnectivityManager connectivityManager;
private FavouritesFragment favouritesFragment;
private FragmentsAdapter fragmentsAdapter;
private SearchFragment searchFragment;
private int searchFragmentPosition;
private SearchView searchView;
private ViewPager viewPager;
private long lastPlaceId;


    protected void onCreate()
    {
    FragmentManager fragmentManager;

    lastPlaceId=AppData.NULL_DATA;

    connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

    fragmentManager=getSupportFragmentManager();

    onCreateFragments(fragmentManager);

    fragmentsAdapter=new FragmentsAdapter( this ,fragmentManager , searchFragment, favouritesFragment );

    searchFragmentPosition=fragmentsAdapter.getItemPosition(searchFragment);

    Log.d("HA-onCreate","defaultLocale= " + Locale.getDefault().toString());
    Log.d("HA-onCreate","resLocale= "+getResources().getConfiguration().locale.toString());
    }


    private void onCreateFragments(FragmentManager fragmentManager)
    {
    List<Fragment> fragments;

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
    }


    protected void onCreateView()
    {
    setContentView(R.layout.activity_home);

    Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            onSearch(null);
            }
        });

    viewPager=(ViewPager) findViewById(R.id.ViewPager);
    viewPager.setAdapter(fragmentsAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_tabs);
    tabLayout.setupWithViewPager(viewPager);

    ViewCompat.setLayoutDirection(tabLayout, ViewCompat.LAYOUT_DIRECTION_LTR);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    MenuItem searchItem;

    getMenuInflater().inflate( R.menu.menu_home_activity, menu);

    searchItem = menu.findItem( R.id.m_search);

    searchView = (SearchView) searchItem.getActionView();
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
        //TODO
        //deleteDatabase(Database.DATABASE_NAME);

        /* remove shared preference file
        File folder=new File(getFilesDir().getParent()+File.separator+"shared_prefs");

        File[] files=folder.listFiles();

        if(files != null)
            {
            for(File file : files)
                {
                file.delete();
                }
            }

        folder.delete();
        */

        break;

        case R.id.m_setting:

        intent=new Intent(this, SettingsActivity.class);
        startActivityForResult(intent,SETTINGS_REQUEST);
        break;

        default:
        isActionDone=false;
        }

    return isActionDone;
    }


    @Override
    protected void onResume()
    {
    super.onResume();

    if(lastPlaceId != AppData.NULL_DATA)
        {
        favouritesFragment.onPlaceChanged(lastPlaceId);
        searchFragment.onPlaceChanged(lastPlaceId);

        lastPlaceId=AppData.NULL_DATA;
        }
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

    if(location!=null)
        {
        Toast.makeText(this, "provider = "+ location.getProvider() , Toast.LENGTH_SHORT).show();
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
    public void openPlaceDetailsActivity(long placeId)
    {
    lastPlaceId=placeId;

    PlaceDetailsActivity.openActivity(this, placeId);
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

}

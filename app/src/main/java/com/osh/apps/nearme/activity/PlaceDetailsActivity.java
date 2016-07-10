package com.osh.apps.nearme.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.osh.apps.nearme.menu.PlaceMenu;
import com.osh.apps.nearme.R;
import com.osh.apps.nearme.activity.callback.PlaceDetailsCallback;
import com.osh.apps.nearme.adapter.FragmentsAdapter;
import com.osh.apps.nearme.app.AppData;
import com.osh.apps.nearme.database.DatabaseManager;
import com.osh.apps.nearme.fragment.DetailsFragment;
import com.osh.apps.nearme.fragment.MapFragment;
import com.osh.apps.nearme.place.Place;

import java.util.List;


public class PlaceDetailsActivity extends BaseActivity implements PlaceDetailsCallback
{
private static final String EXTRA_PLACE_ID="placeId";

private FragmentsAdapter fragmentsAdapter;
private DatabaseManager databaseManager;
private DetailsFragment detailsFragment;
private FragmentManager fragmentManager;
private MapFragment mapFragment;
private int mapFragmentPosition;
private ViewPager viewPager;
private Place place;


    public static void openActivity(Context context, long placeId)
    {
    Intent intent=new Intent(context, PlaceDetailsActivity.class);
    intent.putExtra(EXTRA_PLACE_ID, placeId);
    context.startActivity(intent);
    }


    @Override
    protected void onCreate()
    {
    long placeId;

    detailsFragment=null;
    mapFragment=null;

    databaseManager=DatabaseManager.getInstance(this);

    placeId=getIntent().getLongExtra(EXTRA_PLACE_ID, AppData.NULL_DATA);

    place=databaseManager.getPlace(placeId);
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

            if(fragment instanceof DetailsFragment)
                {
                detailsFragment=(DetailsFragment) fragment;
                continue;
                }

            if(fragment instanceof MapFragment)
                {
                mapFragment=(MapFragment) fragment;
                continue;
                }
            }

        }

    if(detailsFragment == null)
        {
        detailsFragment=DetailsFragment.newInstance(place.getId());
        }

    if(mapFragment == null)
        {
        mapFragment=MapFragment.newInstance(place.getId());
        }

    fragmentsAdapter=new FragmentsAdapter(this, fragmentManager, detailsFragment, mapFragment);

    mapFragmentPosition=fragmentsAdapter.getItemPosition(mapFragment);
    }


    @Override
    protected void onCreateView()
    {
    ActionBar actionBar;

    setContentView(R.layout.activity_place_details);

    Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    actionBar=getSupportActionBar();
    actionBar.setTitle(place.getName());
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);

    viewPager=(ViewPager) findViewById(R.id.ViewPager);
    viewPager.setAdapter(fragmentsAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_tabs);
    tabLayout.setupWithViewPager(viewPager);

    ViewCompat.setLayoutDirection(tabLayout, ViewCompat.LAYOUT_DIRECTION_LTR);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    PlaceMenu.createPlaceMenu(menu, getMenuInflater(), place.isFavourite());

    return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    boolean isFavouritePlace;

    switch (item.getItemId())
        {

        case R.id.m_favourite_toggle:
        isFavouritePlace=!(place.isFavourite());

        databaseManager.updatePlace(place.getId(), isFavouritePlace);
        place.setFavourite(isFavouritePlace);

        if(isFavouritePlace)
            {
            item.setIcon(R.drawable.ic_menu_star);
            }else
                {
                item.setIcon(R.drawable.ic_menu_star_border);
                }
        break;

        case android.R.id.home:
        onBackPressed();
        break;

        case R.id.m_share:
        AppData.sharePlace(this, place);
        break;

        }

    return true;
    }


    @Override
    public void onLocationChanged(Location location)
    {
    detailsFragment.onLocationChanged(location);
    mapFragment.onLocationChanged(location);
    }


    @Override
    public Location getCurrentLocation()
    {
    return getLocation();
    }


    @Override
    public void onClickDetail()
    {
    viewPager.setCurrentItem(mapFragmentPosition, true);
    mapFragment.animatePlaceMarker();
    }
}

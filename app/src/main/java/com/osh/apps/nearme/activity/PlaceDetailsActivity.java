package com.osh.apps.nearme.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.osh.apps.nearme.R;
import com.osh.apps.nearme.activity.callback.PlaceDetailsCallback;
import com.osh.apps.nearme.adapter.FragmentsAdapter;
import com.osh.apps.nearme.app.AppData;
import com.osh.apps.nearme.database.DatabaseManager;
import com.osh.apps.nearme.fragment.DetailsFragment;
import com.osh.apps.nearme.fragment.MapFragment;
import com.osh.apps.nearme.place.Place;


public class PlaceDetailsActivity extends BaseActivity implements PlaceDetailsCallback
{
private static final String EXTRA_PLACE_ID="placeId";

private FragmentsAdapter fragmentsAdapter;
private DatabaseManager databaseManager;
private DetailsFragment detailsFragment;
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


    protected void onCreate()
    {
    long placeId;

    databaseManager=DatabaseManager.getInstance(this);

    placeId=getIntent().getLongExtra(EXTRA_PLACE_ID, AppData.NULL_DATA);

    place=databaseManager.getPlace(placeId);

    detailsFragment=DetailsFragment.newInstance(placeId);

    mapFragment=new MapFragment();

    fragmentsAdapter=new FragmentsAdapter(this, getSupportFragmentManager(), detailsFragment, mapFragment);

    mapFragmentPosition=fragmentsAdapter.getItemPosition(mapFragment);
    }


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

    ViewCompat.setLayoutDirection(tabLayout, View.LAYOUT_DIRECTION_LTR);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    getMenuInflater().inflate(R.menu.menu_place_details, menu);

    menu.findItem(R.id.m_favourite_toggle).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

    return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
    if(place.isFavourite())
        {
        menu.findItem(R.id.m_favourite_toggle).setIcon(R.mipmap.ic_menu_star);
        }else
            {
            menu.findItem(R.id.m_favourite_toggle).setIcon(R.mipmap.ic_menu_star_border);
            }

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

        invalidateOptionsMenu();
        break;

        case android.R.id.home:
        onBackPressed();
        break;

        }

    return true;
    }


    @Override
    public void onLocationChanged(Location location)
    {
    detailsFragment.onLocationChanged(location);

    if(location!=null)
        {
        Toast.makeText(this, "provider = "+ location.getProvider() , Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void showMap()
    {

    }


    @Override
    public void showDetails()
    {

    }


    @Override
    public Location getCurrentLocation()
    {
    return getLocation();
    }
}

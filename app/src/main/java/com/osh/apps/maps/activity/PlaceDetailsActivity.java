package com.osh.apps.maps.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.osh.apps.maps.R;
import com.osh.apps.maps.adapter.FragmentsAdapter;
import com.osh.apps.maps.app.AppData;
import com.osh.apps.maps.fragment.DetailsFragment;
import com.osh.apps.maps.fragment.TabFragment;


public class PlaceDetailsActivity extends AppCompatActivity
{
private static final String EXTRA_PLACE_NAME="placeName";
private static final String EXTRA_PLACE_ID="placeId";


private DetailsFragment detailsFragment;
private TabFragment mapFragment;
private FragmentsAdapter fragmentsAdapter;
private int mapFragmentPosition;
private ViewPager viewPager;


    public static void openActivity(Context context, String placeName, long placeId)
    {
    Intent intent=new Intent(context, PlaceDetailsActivity.class);
    intent.putExtra(EXTRA_PLACE_NAME, placeName);
    intent.putExtra(EXTRA_PLACE_ID, placeId);
    context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_place_details);

    init();

    setViews();
    }


    private void init()
    {
    Intent intent= getIntent();

    detailsFragment=DetailsFragment.newInstance(intent.getLongExtra(EXTRA_PLACE_ID, AppData.NULL_DATA));

    mapFragment=new TabFragment()
        {
            @Override
            public int getTitleRes()
            {
            return R.string.map;
            }
        };

    fragmentsAdapter=new FragmentsAdapter( this, getSupportFragmentManager(), detailsFragment, mapFragment );

    mapFragmentPosition=fragmentsAdapter.getItemPosition(mapFragment);
    }


    private void setViews()
    {
    Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_PLACE_NAME));

    viewPager=(ViewPager) findViewById(R.id.ViewPager);
    viewPager.setAdapter(fragmentsAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_tabs);
    tabLayout.setupWithViewPager(viewPager);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    getMenuInflater().inflate(R.menu.menu_place_details, menu); //TODO
    return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) //TODO
    {
    int id=item.getItemId();


    return super.onOptionsItemSelected(item);
    }
}

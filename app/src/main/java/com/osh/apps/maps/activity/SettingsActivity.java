package com.osh.apps.maps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.osh.apps.maps.R;
import com.osh.apps.maps.fragment.SettingsFragment;


public class SettingsActivity extends AppCompatActivity
{
public static final String KEY_FAVOURITES_REMOVED="isFavouritesRemoved";
public static final String KEY_DISTANCE_TYPE_CHANGED="isDistanceTypeChanged";

private SettingsFragment settingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    ActionBar actionBar=getSupportActionBar();
    actionBar.setTitle(R.string.title_activity_settings);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);

    settingsFragment=new SettingsFragment();

    getFragmentManager().beginTransaction()
                        .replace(R.id.container, settingsFragment)
                        .commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    switch(item.getItemId())
        {

        case android.R.id.home:
        onBackPressed();
        break;

        }

    return true;
    }


    @Override
    public void onBackPressed()
    {
    Intent resultData = new Intent();
    resultData.putExtra(KEY_FAVOURITES_REMOVED, settingsFragment.isFavouritesRemoved());
    resultData.putExtra(KEY_DISTANCE_TYPE_CHANGED, settingsFragment.isDistanceTypeChanged());

    setResult(Activity.RESULT_OK, resultData);

    super.onBackPressed();
    }
}

package com.osh.apps.maps.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.osh.apps.maps.R;
import com.osh.apps.maps.app.AppData;
import com.osh.apps.maps.database.DatabaseManager;
import com.osh.apps.maps.dialog.SimpleAlertDialog;
import com.osh.apps.maps.widget.preference.IntegerListPreference;
import com.osh.apps.maps.widget.preference.SeekBarPreference;


/**
 * Created by oshri-n on 30/06/2016.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, SeekBarPreference.OnSummaryChangeListener, Preference.OnPreferenceClickListener
{
private static final int MIN_RADIUS=100;
private static final int MAX_RADIUS=5000;
private static final int STEP_RADIUS=100;

private boolean isFavouritesRemoved,isDistanceTypeChanged;
private IntegerListPreference distanceType;
private DatabaseManager databaseManager;
private SeekBarPreference searchRadius;
private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    PreferenceManager preferenceManager;
    PreferenceScreen screen;

    isFavouritesRemoved=false;
    isDistanceTypeChanged=false;

    preferenceManager= getPreferenceManager();

    preferenceManager.setSharedPreferencesMode(Context.MODE_PRIVATE);
    preferenceManager.setSharedPreferencesName(AppData.Preferences.SHARED_PREFERENCES_NAME);

    addPreferencesFromResource(R.xml.settings);

    screen=getPreferenceScreen();

    context=screen.getContext();

    databaseManager=DatabaseManager.getInstance(context);

    distanceType=new IntegerListPreference(context);
    distanceType.setKey(AppData.Preferences.KEY_DISTANCE_TYPE);
    distanceType.setDefaultValue(AppData.Preferences.DEFAULT_DISTANCE_TYPE);
    distanceType.setEntries(AppData.Preferences.getDistanceTypes(context));
    distanceType.setEntryValues(AppData.Preferences.getDistanceTypeValues());
    distanceType.setTitle(getString(R.string.title_distance_type));
    distanceType.setSummary(AppData.Preferences.getDistanceTypeText(context));
    distanceType.setOnPreferenceChangeListener(this);

    screen.addPreference(distanceType);

    searchRadius=new SeekBarPreference(context);
    searchRadius.setKey(AppData.Preferences.KEY_RADIUS);
    searchRadius.setDefaultValue(AppData.Preferences.DEFAULT_RADIUS);
    searchRadius.setTitle(R.string.title_search_radius);
    searchRadius.setOnSummaryChangeListener(this);
    searchRadius.setLimitValue(MIN_RADIUS,MAX_RADIUS);
    searchRadius.setStepSize(STEP_RADIUS);
    //searchRadius.setOnPreferenceChangeListener(this);

    screen.addPreference(searchRadius);

    Preference removeFavourites=new Preference(context);
    removeFavourites.setTitle(R.string.title_remove_favourites);
    removeFavourites.setSummary(R.string.summary_remove_favourites);
    removeFavourites.setOnPreferenceClickListener(this);
    screen.addPreference(removeFavourites);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {

    if(preference==distanceType)
        {
        isDistanceTypeChanged=true;
        distanceType.setSummary( AppData.Preferences.getDistanceTypeText(context, Integer.parseInt(newValue.toString()) ) );
        }

    return true;
    }


    @Override
    public String onUpdateSummary(int value)
    {
    return getString(R.string.summary_radius) + " " + AppData.Preferences.getDistanceText(context,value);
    }


    @Override
    public boolean onPreferenceClick(Preference preference)
    {
    SimpleAlertDialog.AlertDialogListener alertDialogListener=new SimpleAlertDialog.AlertDialogListener()
        {
            @Override
            public void onPositive(DialogInterface dialog)
            {
            databaseManager.removeAllFavourites();
            isFavouritesRemoved=true;
            }


            @Override
            public void onNegative(DialogInterface dialog)
            {

            }
        };


    SimpleAlertDialog.createAlertDialog(context, getString(R.string.title_remove_favourites),
                                                 getString(R.string.message_remove_favourites),
                                                 getString(R.string.yes),
                                                 getString(R.string.no),
                                                 alertDialogListener);


    return true;
    }


    public boolean isFavouritesRemoved()
    {
    return isFavouritesRemoved;
    }


    public boolean isDistanceTypeChanged()
    {
    return isDistanceTypeChanged;
    }
}

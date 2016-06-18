package com.osh.apps.maps.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.osh.apps.maps.R;
import com.osh.apps.maps.app.AppData;
import com.osh.apps.maps.database.DatabaseManager;
import com.osh.apps.maps.network.connection.JsonConnection;

import org.json.JSONArray;
import org.json.JSONObject;


public class SearchService extends IntentService
{
public static final String ACTION_SEARCH="com.osh.apps.maps.action.search";

private static final String EXTRA_KEYWORD="keyword";
private static final String EXTRA_LAT="lat";
private static final String EXTRA_LNG="lng";

public static final String EXTRA_STATUS="status";

public static final int STATUS_OK=0;
public static final int STATUS_ZERO_RESULTS=1;
public static final int STATUS_ERROR=2;



    public static void startActionSearch(Context context, String keyword, double lat, double lng)
    {
    Intent intent=new Intent(context, SearchService.class);
    intent.setAction(ACTION_SEARCH);
    intent.putExtra(EXTRA_KEYWORD, keyword);
    intent.putExtra(EXTRA_LAT, lat);
    intent.putExtra(EXTRA_LNG, lng);
    context.startService(intent);
    }


    public SearchService()
    {
    super("SearchService");
    }



    @Override
    protected void onHandleIntent(Intent intent)
    {
    String action;

    if(intent!=null)
        {
        action=intent.getAction();

        if(action.equals(ACTION_SEARCH))
            {
            handleActionSearch(intent.getStringExtra(EXTRA_KEYWORD),intent.getDoubleExtra(EXTRA_LAT, 0),intent.getDoubleExtra(EXTRA_LNG, 0));
            }
        }
    }


    private void handleActionSearch(String keyword, double lat, double lng)
    {
    DatabaseManager databaseManager;
    JSONObject json, place, location;
    JSONArray results;
    String url,jsonStatus;
    Intent intent;
    float rating;
    int status;


    databaseManager=DatabaseManager.getInstance(this);

    databaseManager.removeLastSearch();

    status=STATUS_ERROR;
    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=32.0601678,34.7848986&radius=500&keyword=coffee&key=AIzaSyC5I_AJm7WkW3TXMu3XvLWIn7EvA8Asr2c

    url=AppData.PlacesAPI.createSearchUrl(keyword, lat , lng, 1000, getString(R.string.places_api_key)); //TODO get radius from SharedPreferences

    try {
        json=new JSONObject(JsonConnection.getJsonResult(url));
        results=json.getJSONArray("results");

        for(int i=0 ; i<results.length() ; i++)
                {
                place=results.getJSONObject(i);

                rating=0;

                try {
                    rating=(float) place.getDouble("rating");
                    }catch(Exception e){}

                location=place.getJSONObject("geometry").getJSONObject("location");

                databaseManager.insertSearchPlace(place.getString("place_id"), place.getString("name"), place.getString("vicinity"), location.getDouble("lat"), location.getDouble("lng"), rating);
                }

        jsonStatus= json.getString("status");

        if(jsonStatus.equals("OK"))
            {
            status=STATUS_OK;
            }else if(jsonStatus.equals("ZERO_RESULTS"))
                {
                status=STATUS_ZERO_RESULTS;
                }

        }catch(Exception e)
            {
            e.printStackTrace();
            }

    intent=new Intent(ACTION_SEARCH);
    intent.putExtra(EXTRA_STATUS , status);

    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}

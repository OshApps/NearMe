package com.osh.apps.maps.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.osh.apps.maps.network.connection.JsonConnection;
import com.osh.apps.maps.place.SimplePlaceDetails;

import org.json.JSONArray;
import org.json.JSONObject;


public class SearchService extends IntentService
{
private static final String ACTION_SEARCH="com.osh.apps.maps.action.search";

private static final String EXTRA_QUERY="com.osh.apps.maps.extra.query";


    public static void startActionSearch(Context context, String query)
    {
    Intent intent=new Intent(context, SearchService.class);
    intent.setAction(ACTION_SEARCH);
    intent.putExtra(EXTRA_QUERY, query);
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
            handleActionSearch(intent.getStringExtra(EXTRA_QUERY));
            }
        }
    }


    private void handleActionSearch(String query)
    {
    SimplePlaceDetails[] places;
    JSONObject json, place, location;
    JSONArray results;
    float rating;
    String url;

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=32.0601678,34.7848986&radius=500&keyword=coffee&key=AIzaSyC5I_AJm7WkW3TXMu3XvLWIn7EvA8Asr2c

    if(query!=null)
        {
        url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=32.0601678,34.7848986&radius=500&keyword=coffee&key=AIzaSyC5I_AJm7WkW3TXMu3XvLWIn7EvA8Asr2c";


        try {
            json=new JSONObject(JsonConnection.getJsonResult(url));
            results=json.getJSONArray("results");

            places=new SimplePlaceDetails[results.length()];

            for(int i=0 ; i<results.length() ; i++)
                    {
                    place=results.getJSONObject(i);

                    rating=0;

                    try {
                        rating=(float) place.getDouble("rating");
                        }catch(Exception e){}

                    location=place.getJSONObject("geometry").getJSONObject("location");

                    places[i]= new SimplePlaceDetails(place.getString("place_id"), place.getString("name"), place.getString("vicinity"), location.getDouble("lat"), location.getDouble("lng"), rating);
                    }

            }catch(Exception e)
                {
                e.printStackTrace();
                }


        }
    }
}

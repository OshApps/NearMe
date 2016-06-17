package com.osh.apps.maps.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.osh.apps.maps.database.database.DatabaseSQL;
import com.osh.apps.maps.place.Place;

import java.util.ArrayList;


public class DatabaseManager extends DatabaseSQL
{
private static DatabaseManager instance;

    public synchronized static DatabaseManager getInstance(Context context)
    {

    if(instance==null)
        {
        instance=new DatabaseManager(context);
        }

    return instance;
    }


    private DatabaseManager(Context context)
    {
    super(context);
    }



    private long insertPlace(String tableName, String googleId, String name, String address, double lat, double lng, float rating) throws SQLException
    {
    ContentValues values;

    values=new ContentValues();
    values.put(Table.Places.COL_GOOGLE_ID, googleId);
    values.put(Table.Places.COL_NAME, name);
    values.put(Table.Places.COL_ADDRESS, address);
    values.put(Table.Places.COL_LAT, lat);
    values.put(Table.Places.COL_LNG, lng);
    values.put(Table.Places.COL_RATING, rating);

    return insert(tableName, values);
    }


    public void insertPlaceSearch(String googleId, String name, String address, double lat, double lng, float rating)
    {

    try {
        insertPlace(Table.Places.LastSearch.TABLE_NAME, googleId, name, address, lat, lng, rating);
        }catch (SQLException e)
            {
            Log.e("DatabaseManager","Error: Failed to insert Place into LastSearch table");
            e.printStackTrace();
            }
    }


    public Place insertFavouritePlace(String googleId, String name, String address, double lat, double lng, float rating)
    {
    Place place=null;
    long placeId;


    try {
        placeId=insertPlace(Table.Places.LastSearch.TABLE_NAME, googleId, name, address, lat, lng, rating);

        place=new Place(placeId, googleId, name, address, lat, lng, rating);

        }catch (SQLException e)
            {
            Log.e("DatabaseManager","Error: Failed to insert Place into favourites table");
            e.printStackTrace();
            }


    return place;
    }





    public ArrayList<Place> getLastSearch()
    {
    int searchIdIndex,googleIdIndex,nameIndex,addressIndex,latIndex,lngIndex,ratingIndex;
    ArrayList<Place> places=null;
    Cursor cursor;

    cursor=selectFromTable(Table.Places.LastSearch.TABLE_NAME, null, null);

	if(cursor!=null)
        {
        places=new ArrayList<>();

        searchIdIndex=cursor.getColumnIndex(Table.Places.LastSearch.COL_SEARCH_ID);
        googleIdIndex=cursor.getColumnIndex(Table.Places.COL_GOOGLE_ID);
        nameIndex=cursor.getColumnIndex(Table.Places.COL_NAME);
        addressIndex=cursor.getColumnIndex(Table.Places.COL_ADDRESS);
        latIndex=cursor.getColumnIndex(Table.Places.COL_LAT);
        lngIndex=cursor.getColumnIndex(Table.Places.COL_LNG);
        ratingIndex=cursor.getColumnIndex(Table.Places.COL_RATING);

        do{

          places.add(new Place(cursor.getLong(searchIdIndex),
                               cursor.getString(googleIdIndex),
                               cursor.getString(nameIndex),
                               cursor.getString(addressIndex),
                               cursor.getDouble(latIndex),
                               cursor.getDouble(lngIndex),
                               cursor.getFloat(ratingIndex)
                              ));

          }while(cursor.moveToNext());

        cursor.close();
        }

    return places;
    }


    public void removeLastSearch()
    {
    deleteFromTable(Table.Places.LastSearch.TABLE_NAME, null);
    }


    public Place getFavouritePlace(long placeId)
    {
    Cursor cursor;
    Place place=null;

    cursor=selectFromTable(Table.Places.Favourites.TABLE_NAME, null, addColStatement(Table.Places.Favourites.COL_FAVORITE_ID, String.valueOf(placeId), false));

    if(cursor!=null)
        {
        place=new Place(cursor.getLong(cursor.getColumnIndex(Table.Places.Favourites.COL_FAVORITE_ID)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_GOOGLE_ID)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_NAME)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_ADDRESS)),
                        cursor.getDouble(cursor.getColumnIndex(Table.Places.COL_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(Table.Places.COL_LNG)),
                        cursor.getFloat(cursor.getColumnIndex(Table.Places.COL_RATING))
                        );
        }


    return place;
    }


    public Place getSearchPlace(long placeId)
    {
    Cursor cursor;
    Place place=null;

    cursor=selectFromTable(Table.Places.LastSearch.TABLE_NAME, null, addColStatement(Table.Places.LastSearch.COL_SEARCH_ID, String.valueOf(placeId), false));

    if(cursor!=null)
        {
        place=new Place(cursor.getLong(cursor.getColumnIndex(Table.Places.LastSearch.COL_SEARCH_ID)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_GOOGLE_ID)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_NAME)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_ADDRESS)),
                        cursor.getDouble(cursor.getColumnIndex(Table.Places.COL_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(Table.Places.COL_LNG)),
                        cursor.getFloat(cursor.getColumnIndex(Table.Places.COL_RATING))
                        );
        }


    return place;
    }

}
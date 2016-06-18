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


    public void insertSearchPlace(String googleId, String name, String address, double lat, double lng, float rating)
    {
    ContentValues values;

    try {
        values=new ContentValues();
        values.put(Table.Places.COL_GOOGLE_ID, googleId);
        values.put(Table.Places.COL_NAME, name);
        values.put(Table.Places.COL_ADDRESS, address);
        values.put(Table.Places.COL_LAT, lat);
        values.put(Table.Places.COL_LNG, lng);
        values.put(Table.Places.COL_RATING, rating);
        values.put(Table.Places.COL_FAVOURITE, Table.FALSE);
        values.put(Table.Places.COL_SEARCH, Table.TRUE);

        insert(Table.Places.TABLE_NAME, values);
        }catch (SQLException e)
            {
            Log.e("DatabaseManager","Error: Failed to insert place into places table");
            e.printStackTrace();
            }
    }


    public void updatePlace(long placeId, boolean isFavourite)
    {
    String updateCols,whereCols;

    try {
        updateCols=addColStatement(Table.Places.COL_FAVOURITE, String.valueOf(isFavourite? Table.TRUE: Table.FALSE), false);
        whereCols=addColStatement(Table.Places.COL_PLACE_ID, String.valueOf(placeId), false);

        updateFromTable(Table.Places.TABLE_NAME, updateCols, whereCols);

        //TODO delete if is no favourite or search
        }catch (SQLException e)
            {
            Log.e("DatabaseManager","Error: Failed to update place");
            e.printStackTrace();
            }
    }


    public ArrayList<Place> getFavouritePlaces()
    {
    int placeIdIndex,googleIdIndex,nameIndex,addressIndex,latIndex,lngIndex,ratingIndex;
    ArrayList<Place> places=null;
    Cursor cursor;

    cursor=selectFromTable(Table.Places.TABLE_NAME, null, addColStatement(Table.Places.COL_FAVOURITE, String.valueOf(Table.TRUE), false));

	if(cursor!=null)
        {
        places=new ArrayList<>();

        placeIdIndex=cursor.getColumnIndex(Table.Places.COL_PLACE_ID);
        googleIdIndex=cursor.getColumnIndex(Table.Places.COL_GOOGLE_ID);
        nameIndex=cursor.getColumnIndex(Table.Places.COL_NAME);
        addressIndex=cursor.getColumnIndex(Table.Places.COL_ADDRESS);
        latIndex=cursor.getColumnIndex(Table.Places.COL_LAT);
        lngIndex=cursor.getColumnIndex(Table.Places.COL_LNG);
        ratingIndex=cursor.getColumnIndex(Table.Places.COL_RATING);

        do{

          places.add(new Place(cursor.getLong(placeIdIndex),
                               cursor.getString(googleIdIndex),
                               cursor.getString(nameIndex),
                               cursor.getString(addressIndex),
                               cursor.getDouble(latIndex),
                               cursor.getDouble(lngIndex),
                               cursor.getFloat(ratingIndex),
                               true
                               ));

          }while(cursor.moveToNext());

        cursor.close();
        }

    return places;
    }


    public ArrayList<Place> getLastSearch()
    {
    int placeIdIndex,googleIdIndex,nameIndex,addressIndex,latIndex,lngIndex,ratingIndex,isFavouriteIndex;
    ArrayList<Place> places=null;
    Cursor cursor;

    cursor=selectFromTable(Table.Places.TABLE_NAME, null, addColStatement(Table.Places.COL_SEARCH, String.valueOf(Table.TRUE), false));

	if(cursor!=null)
        {
        places=new ArrayList<>();

        placeIdIndex=cursor.getColumnIndex(Table.Places.COL_PLACE_ID);
        googleIdIndex=cursor.getColumnIndex(Table.Places.COL_GOOGLE_ID);
        nameIndex=cursor.getColumnIndex(Table.Places.COL_NAME);
        addressIndex=cursor.getColumnIndex(Table.Places.COL_ADDRESS);
        latIndex=cursor.getColumnIndex(Table.Places.COL_LAT);
        lngIndex=cursor.getColumnIndex(Table.Places.COL_LNG);
        ratingIndex=cursor.getColumnIndex(Table.Places.COL_RATING);
        isFavouriteIndex=cursor.getColumnIndex(Table.Places.COL_FAVOURITE);


        do{

          places.add(new Place(cursor.getLong(placeIdIndex),
                        cursor.getString(googleIdIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(addressIndex),
                        cursor.getDouble(latIndex),
                        cursor.getDouble(lngIndex),
                        cursor.getFloat(ratingIndex),
                        cursor.getInt(isFavouriteIndex) == Table.TRUE
                        ));

          }while(cursor.moveToNext());

        cursor.close();
        }

    return places;
    }


    public void removeLastSearch()
    {
    deleteFromTable(Table.Places.TABLE_NAME, addColStatement(Table.Places.COL_SEARCH, String.valueOf(Table.TRUE), false));
    }


    public Place getPlace(long placeId)
    {
    Cursor cursor;
    Place place=null;

    cursor=selectFromTable(Table.Places.TABLE_NAME, null, addColStatement(Table.Places.COL_PLACE_ID, String.valueOf(placeId), false));

    if(cursor!=null)
        {
        place=new Place(cursor.getLong(cursor.getColumnIndex(Table.Places.COL_PLACE_ID)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_GOOGLE_ID)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_NAME)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_ADDRESS)),
                        cursor.getDouble(cursor.getColumnIndex(Table.Places.COL_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(Table.Places.COL_LNG)),
                        cursor.getFloat(cursor.getColumnIndex(Table.Places.COL_RATING)),
                        cursor.getInt(cursor.getColumnIndex(Table.Places.COL_FAVOURITE))== Table.TRUE
                        );
        }


    return place;
    }

}
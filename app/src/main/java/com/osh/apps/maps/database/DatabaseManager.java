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


    public void insertSearchPlace(String googleId, String name, String address, double lat, double lng, float rating, String iconUrl, String phone, String website)
    {
    ContentValues values;
    boolean isExist;

    isExist= isExist(Table.Places.TABLE_NAME, addColStatement(Table.Places.COL_GOOGLE_ID, googleId, true));

    if(!isExist)
        {
        try {
            values=new ContentValues();
            values.put(Table.Places.COL_GOOGLE_ID, googleId);
            values.put(Table.Places.COL_NAME, name);
            values.put(Table.Places.COL_ADDRESS, address);
            values.put(Table.Places.COL_LAT, lat);
            values.put(Table.Places.COL_LNG, lng);
            values.put(Table.Places.COL_RATING, rating);
            values.put(Table.Places.COL_ICON_URL, iconUrl);
            values.put(Table.Places.COL_PHONE, phone);
            values.put(Table.Places.COL_WEBSITE, website);
            values.put(Table.Places.COL_FAVOURITE, Table.FALSE);
            values.put(Table.Places.COL_SEARCH, Table.TRUE);

            insert(Table.Places.TABLE_NAME, values);
            }catch (SQLException e)
                {
                Log.e("DatabaseManager","Error: Failed to insert place into places table");
                e.printStackTrace();
                }
        }else
            {
            updateFromTable(Table.Places.TABLE_NAME, addColStatement(Table.Places.COL_SEARCH, String.valueOf(Table.TRUE), true),
                                                     addColStatement(Table.Places.COL_GOOGLE_ID, googleId, true));

            }
    }


    public void updatePlace(long placeId, boolean isFavourite)
    {
    String updateCols,whereCols;

    try {
        updateCols=addColStatement(Table.Places.COL_FAVOURITE, String.valueOf(isFavourite? Table.TRUE: Table.FALSE), false);
        whereCols=addColStatement(Table.Places.COL_PLACE_ID, String.valueOf(placeId), false);

        updateFromTable(Table.Places.TABLE_NAME, updateCols, whereCols);

        if(!isFavourite)
            {
            deleteFromTable(Table.Places.TABLE_NAME, addColStatement(Table.Places.COL_PLACE_ID, String.valueOf(placeId), false) + addColStatement(Table.Places.COL_SEARCH, String.valueOf(Table.FALSE), false, OPERATOR_AND));
            }

        }catch (SQLException e)
            {
            Log.e("DatabaseManager","Error: Failed to update place");
            e.printStackTrace();
            }
    }


    /**
     * where col name = TRUE
     *
     * @param whereColName COL_FAVOURITE or  COL_SEARCH
     *
    */
    public ArrayList<Place> getPlaces(String whereColName)
    {
    int placeIdIndex,googleIdIndex,nameIndex,addressIndex,latIndex,lngIndex,ratingIndex,iconUrlIndex,phoneIndex,websiteIndex,isFavouriteIndex;
    ArrayList<Place> places=null;
    Cursor cursor;

    cursor=selectFromTable(Table.Places.TABLE_NAME, null, addColStatement(whereColName, String.valueOf(Table.TRUE), false));

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
        iconUrlIndex=cursor.getColumnIndex(Table.Places.COL_ICON_URL);
        phoneIndex=cursor.getColumnIndex(Table.Places.COL_PHONE);
        websiteIndex=cursor.getColumnIndex(Table.Places.COL_WEBSITE);
        isFavouriteIndex=cursor.getColumnIndex(Table.Places.COL_FAVOURITE);

        do{

          places.add(new Place(cursor.getLong(placeIdIndex),
                               cursor.getString(googleIdIndex),
                               cursor.getString(nameIndex),
                               cursor.getString(addressIndex),
                               cursor.getDouble(latIndex),
                               cursor.getDouble(lngIndex),
                               cursor.getFloat(ratingIndex),
                               cursor.getString(iconUrlIndex),
                               cursor.getString(phoneIndex),
                               cursor.getString(websiteIndex),
                               cursor.getInt(isFavouriteIndex) == Table.TRUE
                               ));

          }while(cursor.moveToNext());

        cursor.close();
        }

    return places;
    }


    public ArrayList<Place> getFavouritePlaces()
    {
    return getPlaces(Table.Places.COL_FAVOURITE);
    }


    public ArrayList<Place> getLastSearch()
    {
    return getPlaces(Table.Places.COL_SEARCH);
    }


    public void removeLastSearch()
    {
    updateFromTable(Table.Places.TABLE_NAME, addColStatement(Table.Places.COL_SEARCH, String.valueOf(Table.FALSE), false) , null);
    deleteFromTable(Table.Places.TABLE_NAME, addColStatement(Table.Places.COL_FAVOURITE, String.valueOf(Table.FALSE), false));
    }


    public void removeAllFavourites()
    {
    updateFromTable(Table.Places.TABLE_NAME, addColStatement(Table.Places.COL_FAVOURITE, String.valueOf(Table.FALSE), false) , null);
    deleteFromTable(Table.Places.TABLE_NAME, addColStatement(Table.Places.COL_SEARCH, String.valueOf(Table.FALSE), false));
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
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_ICON_URL)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_PHONE)),
                        cursor.getString(cursor.getColumnIndex(Table.Places.COL_WEBSITE)),
                        cursor.getInt(cursor.getColumnIndex(Table.Places.COL_FAVOURITE))== Table.TRUE
                        );
        }


    return place;
    }


    public boolean isFavouritePlace(long placeId)
    {
    return isExist(Table.Places.TABLE_NAME, addColStatement(Table.Places.COL_PLACE_ID, String.valueOf(placeId), false)+
                                            addColStatement(Table.Places.COL_FAVOURITE, String.valueOf(Table.TRUE), false, OPERATOR_AND));
    }



}
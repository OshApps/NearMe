package com.osh.apps.maps.database.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public abstract class Database extends SQLiteOpenHelper
{
public static final String DATABASE_NAME="Osh-Maps.db";
public static final int DATABASE_VERSION=1;


    public Database(Context context)
    {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


	@Override
	public void onCreate(SQLiteDatabase db)
	{
    db.execSQL("CREATE TABLE "+Table.Places.Favourites.TABLE_NAME+"("+Table.Places.Favourites.COL_FAVORITE_ID+" INTEGER PRIMARY KEY, "+
                                                              Table.Places.COL_GOOGLE_ID+" TEXT," +
                                                              Table.Places.COL_NAME+" TEXT," +
                                                              Table.Places.COL_ADDRESS+" TEXT," +
                                                              Table.Places.COL_LAT+" DOUBLE," +
                                                              Table.Places.COL_LNG+" DOUBLE," +
                                                              Table.Places.COL_RATING+" FLOAT)" +
                                                              ";");


    db.execSQL("CREATE TABLE "+Table.Places.LastSearch.TABLE_NAME+"("+Table.Places.LastSearch.COL_SEARCH_ID+" INTEGER PRIMARY KEY, "+
                                                              Table.Places.COL_GOOGLE_ID+" TEXT," +
                                                              Table.Places.COL_NAME+" TEXT," +
                                                              Table.Places.COL_ADDRESS+" TEXT," +
                                                              Table.Places.COL_LAT+" DOUBLE," +
                                                              Table.Places.COL_LNG+" DOUBLE," +
                                                              Table.Places.COL_RATING+" FLOAT)" +
                                                              ";");
	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}


    protected long insert(String tableName, ContentValues values) throws SQLException
    {
    long id;
    SQLiteDatabase db=getWritableDatabase();

    try {

		id=db.insertOrThrow(tableName, null, values);

	    }

    finally
		{
	    db.close();
		}

    return id;
    }


    protected boolean updateDatabase(String sql)
    {
    SQLiteDatabase db=getWritableDatabase();
    boolean flag = true;

    try {

        db.execSQL(sql);

        } catch (Exception e)
            {
            Log.e("Database", "error in Database-updateDatabase sql=" + sql + " :" + e.getMessage());
            flag = false;
            }
    finally
		{
	    db.close();
		}

    return flag;
    }


    protected Cursor getCursor(String sql)
    {
    SQLiteDatabase db=getReadableDatabase();
    Cursor cursor=null;

    try {
        cursor =  db.rawQuery(sql,null);

        if (!cursor.moveToFirst())
            {
            cursor.close();
            cursor=null;
            }

        }catch (Exception e)
            {
            Log.e("Database", "error in Database-getCursor sql=" + sql + " :" + e.getMessage());

            if(cursor!=null)
                {
                cursor.close();
                }
            }

    return cursor;
    }


    protected boolean isExist(String sql)
    {
    boolean isExist=false;
    Cursor cursor=getCursor(sql);

    if(cursor!=null)
        {
        isExist=true;
        cursor.close();
        }


    return isExist;
	}


    protected static final class Table
    {

        public static class Places
        {
        public static final String COL_GOOGLE_ID="googleID";
        public static final String COL_NAME="name";
        public static final String COL_ADDRESS="address";
        public static final String COL_LAT="lat";
        public static final String COL_LNG="lng";;
        public static final String COL_RATING="rating";


            public static final class Favourites
            {
            public static final String TABLE_NAME="favourites";
            public static final String COL_FAVORITE_ID="favouriteID";
            }


            public static final class LastSearch
            {
            public static final String TABLE_NAME="last_search";
            public static final String COL_SEARCH_ID="searchID";
            }

        }
    }
}

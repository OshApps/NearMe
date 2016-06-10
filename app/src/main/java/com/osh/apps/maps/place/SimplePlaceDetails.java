package com.osh.apps.maps.place;


import com.osh.apps.maps.app.AppData;


/**
 * Created by oshri-n on 16/05/2016.
 */
public class SimplePlaceDetails
{
private String googleId, name, address;
private double lat, lng;
private float rating;
private long id;


    public SimplePlaceDetails(String googleId, String name, String address, double lat, double lng, float rating)
    {
    this(AppData.NULL_DATA, googleId, name, address, lat, lng, rating);
    }


    public SimplePlaceDetails(long id, String googleId, String name, String address, double lat, double lng, float rating)
    {
    this.id=id;
    this.googleId=googleId;
    this.name=name;
    this.address=address;
    this.rating=rating;
    this.lat=lat;
    this.lng=lng;
    }


    public String getGoogleId()
    {
    return googleId;
    }


    public String getName()
    {
    return name;
    }


    public String getAddress()
    {
    return address;
    }


    public float getRating()
    {
    return rating;
    }


    public long getId()
    {
    return id;
    }


    public void setId(long id)
    {
    this.id = id;
    }


    public void setName(String name)
    {
    this.name=name;
    }


    public void setAddress(String address)
    {
    this.address=address;
    }


    public void setRating(float rating)
    {
    this.rating = rating;
    }


    public boolean hasDatabaseID()
    {
    return id!=AppData.NULL_DATA;
    }
}

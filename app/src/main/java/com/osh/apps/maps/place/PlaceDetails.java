package com.osh.apps.maps.place;

/**
 * Created by oshri-n on 14/06/2016.
 */
public class PlaceDetails
{
private String phone,website;
private String[] photoUrls;
private Place place;



    public PlaceDetails(Place place, String phone, String website, String[] photoUrls)
    {
    this.place=place;
    this.phone=phone;
    this.website=website;
    this.photoUrls=photoUrls;
    }


    public String getPhone()
    {
    return phone;
    }


    public String getWebsite()
    {
    return website;
    }


    public Place getPlace()
    {
    return place;
    }


    public String[] getPhotoUrls()
    {
    return photoUrls;
    }
}

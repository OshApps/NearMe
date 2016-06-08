package com.osh.apps.maps.place;

/**
 * Created by oshri-n on 22/05/2016.
 */
public class PlaceDetailsBinder //extends SimplePlaceDetails
{

/*
    public PlaceDetailsBinder(String imdbId, String title, String year, String posterURL, float rating)
    {
    super(imdbId, title, year, posterURL, rating);

    init();
    }


    public PlaceDetailsBinder(long id, String imdbId, String title, String year, String posterURL, float rating)
    {
    super(id, imdbId, title, year, posterURL, rating);

    init();
    }


    private void init()
    {
    loadListener=null;
    poster=null;

    posterRequest=new PosterRequest(AppData.ITEM_POSTER_WIDTH, AppData.ITEM_POSTER_HEIGHT, this);
    }


    public void setOnLoadListener(@NonNull OnLoadListener loadListener)
    {
    this.loadListener = loadListener;

    if(posterRequest.isDone())
        {
        loadListener.preLoad();//TODO remove if no need
        loadListener.onLoad(poster);
        }else
            {
            posterRequest.post(getPosterURL());
            }
    }


    @Override
    public void preLoad()
    {
    loadListener.preLoad();
    }


    @Override
    public void onLoad(Bitmap bitmap)
    {
    loadListener.onLoad(bitmap);

    poster=bitmap;
    }*/
}

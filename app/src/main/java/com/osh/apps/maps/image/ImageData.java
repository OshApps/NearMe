package com.osh.apps.maps.image;

import android.graphics.Bitmap;


/**
 * Created by oshri-n on 16/06/2016.
 */
public class ImageData
{
private boolean isDone;
private Bitmap image;
private String url;


    public ImageData(String url)
    {
    this.url=url;

    image=null;
    isDone=false;
    }


    public String getUrl()
    {
    return url;
    }


    public void setImage(Bitmap image)
    {
    this.image=image;
    isDone=true;
    }


    public Bitmap getImage()
    {
    return image;
    }


    public boolean isDone()
    {
    return isDone;
    }
}

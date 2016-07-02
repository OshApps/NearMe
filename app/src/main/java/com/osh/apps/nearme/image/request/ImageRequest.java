package com.osh.apps.nearme.image.request;

import android.graphics.Bitmap;

import com.osh.apps.nearme.image.data.ImageData;


/**
 * Created by oshri-n on 14/06/2016.
 */
public class ImageRequest
{
private boolean isRequested;
private int width, height;
private ImageData image;


    public ImageRequest(ImageData image, int width, int height)
    {
    this.image=image;
    this.width=width;
    this.height=height;

    isRequested=false;

    if(image.isLoaded())
        {
        onLoad(image.getImage());
        }
    }


    public int getHeight()
    {
    return height;
    }


    public int getWidth()
    {
    return width;
    }


    public String getUrl()
    {
    return image.getUrl();
    }


    public boolean isRequested()
    {
    return isRequested;
    }


    public void onRequested()
    {
    onPreLoad();

    isRequested=true;
    }


    public void onPreLoad()
    {
    image.onPreLoad();
    }


    public void onLoad(Bitmap bitmap)
    {
    image.onLoad(bitmap);
    }
}

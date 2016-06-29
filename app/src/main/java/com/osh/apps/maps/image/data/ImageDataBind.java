package com.osh.apps.maps.image.data;

import android.widget.ImageView;

import com.osh.apps.maps.image.listener.OnLoadListener;
import com.osh.apps.maps.image.listener.OnUnbindListener;


/**
 * Created by oshri-n on 29/06/2016.
 */
public class ImageDataBind extends ImageData
{
private OnUnbindListener bindListener;


    public ImageDataBind(String url)
    {
    super(url);
    }


    public void bind(ImageView imageView, OnLoadListener loadListener, OnUnbindListener bindListener)
    {
    unbind();

    this.bindListener=bindListener;

    setOnLoadListener(loadListener);
    setImageView(imageView);
    }


     public void unbind()
     {

     if(bindListener!=null)
         {
         bindListener.onUnbind();
         bindListener=null;
         }
     }
}

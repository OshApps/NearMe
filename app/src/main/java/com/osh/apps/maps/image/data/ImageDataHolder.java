package com.osh.apps.maps.image.data;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.osh.apps.maps.image.listener.OnLoadListener;
import com.osh.apps.maps.image.listener.OnUnbindListener;


/**
 * Created by oshri-n on 28/06/2016.
 */
public class ImageDataHolder implements OnUnbindListener
{
private OnLoadListener loadListener;
private ImageDataBind imageData;
private ImageView imageView;


    public ImageDataHolder(@Nullable ImageView imageView)
    {
    this(imageView, null);
    }


    public ImageDataHolder(@Nullable ImageView imageView, @Nullable OnLoadListener loadListener)
    {
    this.imageView=imageView;
    this.loadListener=loadListener;

    imageData=null;
    }


    public void bindImageData(ImageDataBind imageData, int width, int height)
    {

    if(this.imageData != null)
        {
        this.imageData.unbind();
        }

    this.imageData=imageData;

    imageData.bind(imageView, loadListener, this);

    if(!imageData.isLoaded())
        {
        if(loadListener != null)
            {
            loadListener.onPreLoad();
            }

        imageData.load(width, height);
        }else if(loadListener != null)
            {
            loadListener.onLoad();
            }
    }


    @Override
    public void onUnbind()
    {
    imageData=null;

    if(imageView != null)
        {
        imageView.setImageBitmap(null);
        }

    }


    public void setOnLoadListener(@Nullable OnLoadListener loadListener)
    {
    this.loadListener=loadListener;

    if(imageData != null)
        {
        imageData.setOnLoadListener(loadListener);
        }
    }


    public void setImageView(@Nullable ImageView imageView)
    {
    this.imageView=imageView;

    if(imageData != null)
        {
        imageData.setImageView(imageView);
        }
    }



}


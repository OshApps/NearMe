package com.osh.apps.maps.image;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


/**
 * Created by oshri-n on 14/06/2016.
 */
public class ImageRequest implements OnLoadListener
{
private ImageData image;
private ImageView imageView;
private ProgressBar loading ;
private int width, height;


    public ImageRequest(ImageData image, ImageView imageView, ProgressBar loading, int width, int height)
    {
    this.image=image;
    this.imageView=imageView;
    this.loading=loading;
    this.width=width;
    this.height=height;

    if(image.isDone())
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


    public ProgressBar getLoading()
    {
    return loading;
    }


    public ImageView getImageView()
    {
    return imageView;
    }


    public String getUrl()
    {
    return image.getUrl();
    }


    public boolean isDone()
    {
    return image.isDone();
    }


    @Override
    public void preLoad()
    {
    loading.setVisibility(View.VISIBLE);
    imageView.setImageBitmap(null);
    }


    @Override
    public void onLoad(Bitmap bitmap)
    {
    image.setImage(bitmap);

    loading.setVisibility(View.GONE);
    imageView.setImageBitmap(bitmap);
    }
}

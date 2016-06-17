package com.osh.apps.maps.image;

import android.graphics.Bitmap;


/**
 * Created by oshri-n on 21/05/2016.
 */
public interface OnLoadListener
{
    void preLoad();
    void onLoad(Bitmap bitmap);
}

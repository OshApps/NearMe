package com.osh.apps.maps.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.osh.apps.maps.R;
import com.osh.apps.maps.image.ImageData;
import com.osh.apps.maps.image.ImageLoaderManager;


/**
 * Created by oshri-n on 14/06/2016.
 */
public class PhotosAdapter extends PagerAdapter
{
private LayoutInflater inflater;
private ImageData[] images;


    public PhotosAdapter(Context context)
    {
    inflater= LayoutInflater.from(context);

    images=null;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
    View viewItem= inflater.inflate(R.layout.vp_photo_item, null);

    ImageView imageView=(ImageView) viewItem.findViewById(R.id.iv_photo);
    ProgressBar loading=(ProgressBar) viewItem.findViewById(R.id.pb_loading);

    ImageLoaderManager.loadImage(images[position], imageView, loading, container.getWidth(), container.getHeight());

    container.addView(viewItem);

    return viewItem;
    }


    public void setImages(String[] photoUrls)
    {
    if(photoUrls!=null)
        {
        images=new ImageData[photoUrls.length];

        for(int i=0 ; i<photoUrls.length ; i++)
            {
            images[i]=new ImageData(photoUrls[i]);
            }
        }

    notifyDataSetChanged();
    }


    @Override
    public int getCount()
    {
    int count=0;

    if(images!=null)
        {
        count=images.length;
        }

    return count;
    }


    @Override
    public boolean isViewFromObject(View view, Object object)
    {
    return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
    container.removeView((View) object);
    }
}

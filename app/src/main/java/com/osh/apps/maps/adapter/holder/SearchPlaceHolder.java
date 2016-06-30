package com.osh.apps.maps.adapter.holder;

import android.view.View;
import android.widget.ImageView;

import com.osh.apps.maps.R;
import com.osh.apps.maps.place.Place;


/**
 * Created by oshri-n on 20/06/2016.
 */
public class SearchPlaceHolder extends PlaceHolder
{
public static final int LAYOUT_RES=R.layout.rv_search_place_item;

private ImageView favouriteIcon;


    public SearchPlaceHolder(View itemView)
    {
    super(itemView);

    favouriteIcon=(ImageView) itemView.findViewById(R.id.iv_favourite_icon);
    }


    @Override
    public void bindPlace(Place place)
    {
    super.bindPlace(place);

    int visible= View.GONE;

    if(place.isFavourite())
        {
        visible=View.VISIBLE;
        }

    favouriteIcon.setVisibility(visible);
    }
}

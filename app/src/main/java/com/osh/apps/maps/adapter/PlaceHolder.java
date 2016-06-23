package com.osh.apps.maps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.osh.apps.maps.R;
import com.osh.apps.maps.place.Place;


/**
 * Created by oshri-n on 20/06/2016.
 */
public class PlaceHolder extends RecyclerView.ViewHolder
{
public static final int LAYOUT_RES=R.layout.rv_favourite_place_item;

private TextView name,address;
private RatingBar rating;


    public PlaceHolder(View itemView)
    {
    super(itemView);

    name=(TextView) itemView.findViewById(R.id.tv_name);
    address=(TextView) itemView.findViewById(R.id.tv_address);

    rating=(RatingBar) itemView.findViewById(R.id.rb_rating);
    }


    public void bindPlace(Place place)
    {
    name.setText(place.getName());
    address.setText(place.getAddress());

    rating.setRating(place.getRating());
    }
}

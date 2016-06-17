package com.osh.apps.maps.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.osh.apps.maps.R;
import com.osh.apps.maps.place.Place;

import java.util.ArrayList;


/**
 * Created by oshri-n on 16/05/2016.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceHolder>
{
private ArrayList<Place> places;
private LayoutInflater inflater;
private int layout;


    public PlaceAdapter(Context context, int layout)
    {
    this.layout=layout;

    inflater = LayoutInflater.from(context);

    places= new ArrayList<>();
    }


    @Override
    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
    View itemView;

    itemView=inflater.inflate(layout, null);

    return new PlaceHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PlaceHolder holder, int position)
    {
    holder.bindPlaceDetails(places.get(position));
    }


    public Place getItem(int position)
    {
    return places.get(position);
    }


    public void setPlaces(ArrayList<Place> places)
    {
    clearPlaces();
    addPlaces(places);
    }


    public void addPlaces(ArrayList<Place> places)
    {
    int lastItemPosition;

    if(places!=null)
        {
        lastItemPosition=this.places.size();

        this.places.addAll(places);

        notifyItemRangeInserted(lastItemPosition, places.size());
        }
    }


    public void removePlace(int position)
    {
    places.remove(position);
    notifyItemRemoved(position);
    }


    public void clearPlaces()
    {
    int size;

    if(!places.isEmpty())
        {
        size=places.size();
        places.clear();

        notifyItemRangeRemoved(0 , size);
        }
    }


    @Override
    public int getItemCount()
    {
    return places.size();
    }


    public boolean isEmpty()
    {
    return places.isEmpty();
    }


    public class PlaceHolder extends RecyclerView.ViewHolder
    {
    private TextView name,address;
    private RatingBar rating;


        public PlaceHolder(View itemView)
        {
        super(itemView);

        name=(TextView) itemView.findViewById(R.id.tv_name);
        address=(TextView) itemView.findViewById(R.id.tv_address);

        rating=(RatingBar) itemView.findViewById(R.id.rb_rating);
        }


        public void bindPlaceDetails(Place placeDetails)
        {
        name.setText(placeDetails.getName());
        address.setText(placeDetails.getAddress());

        rating.setRating(placeDetails.getRating());
        }

    }
}

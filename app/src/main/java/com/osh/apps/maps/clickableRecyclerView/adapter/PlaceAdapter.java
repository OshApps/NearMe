package com.osh.apps.maps.clickableRecyclerView.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.osh.apps.maps.R;
import com.osh.apps.maps.place.SimplePlaceDetails;

import java.util.ArrayList;


/**
 * Created by oshri-n on 16/05/2016.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MovieHolder>
{
private ArrayList<SimplePlaceDetails> places;
private LayoutInflater inflater;
private int layout;


    public PlaceAdapter(Context context, int layout)
    {
    this.layout=layout;

    inflater = LayoutInflater.from(context);

    places= new ArrayList<>();
    }


    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
    View itemView;

    itemView=inflater.inflate(layout, null);

    return new MovieHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MovieHolder holder, int position)
    {
    holder.bindPlaceDetails(places.get(position));
    }


    public SimplePlaceDetails getItem(int position)
    {
    return places.get(position);
    }


    public void setPlaces(@NonNull ArrayList<SimplePlaceDetails> places)
    {
    clearMovies();
    addMovies(places);
    }


    public void addMovies(@NonNull ArrayList<SimplePlaceDetails> movies)
    {
    int lastItemPosition;

    if(movies!=null)
        {
        lastItemPosition=this.places.size();

        this.places.addAll(movies);

        notifyItemRangeInserted(lastItemPosition, movies.size());
        }
    }


    public void removeMovie(int positoin)
    {
    places.remove(positoin);
    notifyItemRemoved(positoin);
    }


    public void clearMovies()
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


    public class MovieHolder extends RecyclerView.ViewHolder
    {
    private TextView name,address;
    private RatingBar rating;


        public MovieHolder(View itemView)
        {
        super(itemView);

        name=(TextView) itemView.findViewById(R.id.tv_name);
        address=(TextView) itemView.findViewById(R.id.tv_address);

        rating=(RatingBar) itemView.findViewById(R.id.rb_rating);
        }


        public void bindPlaceDetails(SimplePlaceDetails placeDetails)
        {
        name.setText(placeDetails.getName());
        address.setText(placeDetails.getAddress());

        rating.setRating(placeDetails.getRating());
        }

    }
}

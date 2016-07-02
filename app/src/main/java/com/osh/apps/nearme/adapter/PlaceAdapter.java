package com.osh.apps.nearme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osh.apps.nearme.adapter.holder.PlaceHolder;
import com.osh.apps.nearme.adapter.holder.SearchPlaceHolder;
import com.osh.apps.nearme.app.AppData;
import com.osh.apps.nearme.place.Place;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by oshri-n on 16/05/2016.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceHolder>
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
    PlaceHolder viewHolder;
    View itemView;

    itemView=inflater.inflate(layout, null);

    if(layout == SearchPlaceHolder.LAYOUT_RES)
        {
        viewHolder= new SearchPlaceHolder(itemView);
        }else
            {
            viewHolder= new PlaceHolder(itemView);
            }

    return viewHolder;
    }


    @Override
    public void onBindViewHolder(PlaceHolder holder, int position)
    {
    holder.bindPlace(places.get(position));
    }


    public int getItemPosition(long placeId)
    {
    int position=AppData.NULL_DATA;

    for(int i=0; i < places.size() ; i++)
        {
        if(places.get(i).getId()==placeId)
            {
            position=i;
            break;
            }
        }

    return position;
    }


    public Place getItem(int position)
    {
    Place place=null;

    if(position >= 0 && position < places.size())
        {
        place=places.get(position);
        }

    return place;
    }


    public Place getItem(long placeId)
    {
    Place placeItem=null;

    for(Place place: places)
        {

        if(place.getId() == placeId)
            {
            placeItem=place;
            break;
            }
        }

    return placeItem;
    }


    @Override
    public long getItemId(int position)
    {
    long placeId=AppData.NULL_DATA;

    if(position >= 0 && position < places.size())
        {
        placeId=places.get(position).getId();
        }

    return placeId;
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


    public void addPlace(Place place)
    {

    if(place!=null)
        {
        places.add(place);

        notifyItemInserted(places.size()-1);
        }
    }


    public void removePlace(long placeId)
    {
    removePlace(getItemPosition(placeId));
    }


    public void removePlace(int position)
    {
    Place place;

    if(position >= 0 && position < places.size())
        {
        place=places.remove(position);
        notifyItemRemoved(position);

        place.removeIconImage();
        }
    }


    public void unfavouriteAllPlaces()
    {

    for(Place place: places)
        {
        place.setFavourite(false);
        }

    refresh();
    }


    public void updateDistance(double lat, double lng)
    {

    for(Place place: places)
        {
        place.updateDistance(lat, lng);
        }
    }


    public void refresh()
    {
    notifyItemRangeChanged( 0, places.size());
    }


    public void clearPlaces()
    {
    Iterator<Place> iterator;
    Place place;
    int size;

    if(!places.isEmpty())
        {
        size=places.size();

        iterator=places.iterator();

        while(iterator.hasNext())
            {
            place= iterator.next();

            place.removeIconImage();

            iterator.remove();
            }

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



}

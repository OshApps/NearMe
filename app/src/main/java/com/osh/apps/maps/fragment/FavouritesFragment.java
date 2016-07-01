package com.osh.apps.maps.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.osh.apps.maps.R;
import com.osh.apps.maps.activity.callback.HomeActivityCallback;
import com.osh.apps.maps.adapter.PlaceAdapter;
import com.osh.apps.maps.database.DatabaseManager;
import com.osh.apps.maps.place.Place;
import com.osh.apps.maps.widget.recyclerview.CustomRecyclerView;


public class FavouritesFragment extends BaseFragment implements CustomRecyclerView.OnItemClickListener, CustomRecyclerView.OnItemLongClickListener
{
private static final int TITLE_RES=R.string.title_tab_favourites;

private HomeActivityCallback homeActivityCallback;
private CustomRecyclerView recyclerView;
private DatabaseManager databaseManager;
private PlaceAdapter adapter;
private PopupMenu popupMenu;
private TextView msg;


    public FavouritesFragment()
    {
    // Required empty public constructor
    }


    public static FavouritesFragment newInstance()
    {
    FavouritesFragment fragment=new FavouritesFragment();
    return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    Location location;

    databaseManager=DatabaseManager.getInstance(getContext());

    popupMenu=null;

    adapter=new PlaceAdapter(getContext(), R.layout.rv_favourite_place_item);
    adapter.setPlaces(databaseManager.getFavouritePlaces());

    location=homeActivityCallback.getCurrentLocation();

    if(location!=null)
        {
        adapter.updateDistance(location.getLatitude(),location.getLongitude());
        }

    onLocationChanged(homeActivityCallback.getCurrentLocation());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    // Inflate the layout for this fragment
    View view=inflater.inflate(R.layout.fragment_base_list_places, container, false);

    recyclerView=(CustomRecyclerView) view.findViewById(R.id.RecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);
    recyclerView.setOnItemClickListener(this);
    recyclerView.setOnItemLongClickListener(this);

    msg=(TextView) view.findViewById(R.id.tv_message);

    return view;
    }


    @Override
    public void onAttach(Context context)
    {
    super.onAttach(context);

    if(context instanceof HomeActivityCallback)
            {
            homeActivityCallback=(HomeActivityCallback) context;
            }else
                {
                throw new RuntimeException(context.toString()+" must implement HomeActivityCallback");
                }
    }


    @Override
    public void onDetach()
    {
    super.onDetach();

    homeActivityCallback=null;
    }


    @Override
    public void onResume()
    {
    super.onResume();

    updateMessage();
    }


    public void refresh()
    {
    if(isCreated())
        {
        adapter.refresh();
        }
    }


    public void onFavouritesRemoved()
    {
    if(isCreated())
        {
        adapter.clearPlaces();
        }
    }


    public void onLocationChanged(Location location)
    {
    if(location!=null && isCreated())
        {
        adapter.updateDistance(location.getLatitude(),location.getLongitude());
        adapter.refresh();
        }
    }


    private void updateMessage()
    {

    if(adapter.isEmpty())
        {
        msg.setText(getString(R.string.no_favourite_place));
        msg.setVisibility(View.VISIBLE);
        }else
            {
            msg.setVisibility(View.GONE);
            }
    }


    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int position, int viewType)
    {
    openPlaceDetailsActivity(position);
    }


    @Override
    public void onItemLongClick(RecyclerView.ViewHolder viewHolder, final int position, int viewType)
    {

    if(popupMenu==null)
        {
        popupMenu= new PopupMenu(getContext(), viewHolder.itemView);
        popupMenu.inflate(R.menu.popup_menu_item);

        popupMenu.getMenu().findItem(R.id.p_delete).setVisible(true);


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                switch(item.getItemId())
                    {
                    case R.id.p_open:
                    openPlaceDetailsActivity(position);
                    break;

                    case R.id.p_delete:
                    deletePlace(position);
                    break;

                    }

                return true;
                }
            });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener()
            {
                @Override
                public void onDismiss(PopupMenu menu)
                {
                popupMenu=null;
                }
            });

        popupMenu.show();
        }
    }


    public void deletePlace(int position)
    {
    long placeId;

    placeId=adapter.getItemId(position);
    adapter.removePlace(position);

    databaseManager.updatePlace(placeId, false);

    homeActivityCallback.onRemoveFavouritePlace(this, placeId);

    updateMessage();
    }


    public void onPlaceChanged(long placeId)
    {
    boolean isFavouritePlace;
    Place place;

    isFavouritePlace=databaseManager.isFavouritePlace(placeId);

    place=adapter.getItem(placeId);

    if(isFavouritePlace && place==null)
        {
        place=databaseManager.getPlace(placeId);
        onFavouritePlaceAdded(place);
        }else if(!isFavouritePlace && place!=null)
            {
            adapter.removePlace(placeId);
            }
    }


    public void onFavouritePlaceAdded(Place place)
    {
    adapter.addPlace(place);
    updateMessage();
    }


    public void onFavouritePlaceRemoved(long placeId)
    {
    adapter.removePlace(placeId);
    }


    private void openPlaceDetailsActivity(int position)
    {
    Place place;

    place=adapter.getItem(position);

    homeActivityCallback.openPlaceDetailsActivity(place.getId());
    }


    @Override
    public int getTitleRes()
    {
    return TITLE_RES;
    }

}

package com.osh.apps.maps.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.osh.apps.maps.HomeActivityCallback;
import com.osh.apps.maps.R;
import com.osh.apps.maps.adapter.PlaceAdapter;
import com.osh.apps.maps.app.AppData;
import com.osh.apps.maps.database.DatabaseManager;
import com.osh.apps.maps.place.Place;
import com.osh.apps.maps.widget.recyclerview.CustomRecyclerView;


public class FavouritesFragment extends TabFragment implements CustomRecyclerView.OnItemClickListener, CustomRecyclerView.OnItemLongClickListener
{
public static final int FRAGMENT_ID=1;

private static final int TITLE_RES=R.string.favourites_tab;

private HomeActivityCallback homeActivityCallback;
private CustomRecyclerView recyclerView;
private DatabaseManager databaseManager;
private int lastItemClickedPosition;
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

    databaseManager=DatabaseManager.getInstance(getContext());

    lastItemClickedPosition=AppData.NULL_DATA;

    popupMenu=null;

    adapter=new PlaceAdapter(getContext(), R.layout.rv_favourite_place_item);
    adapter.setPlaces(databaseManager.getFavouritePlaces());
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
    lastItemClickedPosition=position;
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

    if(homeActivityCallback!=null)
        {
        homeActivityCallback.onRemoveFavouritePlace(FRAGMENT_ID, placeId);
        }

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

    lastItemClickedPosition=position;

    place=adapter.getItem(position);

    homeActivityCallback.openPlaceDetailsActivity(place.getId(), place.getName());
    }


    @Override
    public int getTitleRes()
    {
    return TITLE_RES;
    }
}

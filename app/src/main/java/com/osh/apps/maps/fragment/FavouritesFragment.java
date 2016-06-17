package com.osh.apps.maps.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.osh.apps.maps.R;
import com.osh.apps.maps.adapter.PlaceAdapter;
import com.osh.apps.maps.place.Place;
import com.osh.apps.maps.widget.recyclerview.CustomRecyclerView;

import java.util.ArrayList;


public class FavouritesFragment extends TabFragment
{
private static final int TITLE_RES=R.string.favourites_tab;

private CustomRecyclerView recyclerView;
private PlaceAdapter adapter;
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

    ArrayList<Place> places=new ArrayList<>();

    /*
    places.add(new Place(-1,"22fdgds55","Coffee", "jdkdk ,ljso 25", 3.5f));
    places.add(new Place(-1,"22fdgds55","Rest", "jdklnduis , jdjk ,msk sk 68128255545555", 4.0f));
    places.add(new Place(-1,"22fdgds55","Google", "jdkdk ,ljso 25", 5.0f));
    places.add(new Place(-1,"22fdgds55","Temp", "fghdfg jso 1235", 1.5f));
    places.add(new Place(-1,"22fdgds55","Test", "jfgd hdfdo 6225", 3));
    places.add(new Place(-1,"22fdgds55","Big Bang", "jdkdk dfgd 5", 0));
    places.add(new Place(-1,"22fdgds55","Pizza", "jdkdk ,ld ssa 723", 0.5f));
    */

    adapter=new PlaceAdapter(getContext(), R.layout.rv_place_item);
    adapter.setPlaces(places);
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

    msg=(TextView) view.findViewById(R.id.tv_message);

    return view;
    }


    @Override
    public void onResume()
    {
    super.onResume();

    Log.d("FavouritesFragment","onResume");

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
    public int getTitleRes()
    {
    return TITLE_RES;
    }
}

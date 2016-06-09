package com.osh.apps.maps.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osh.apps.maps.R;
import com.osh.apps.maps.clickableRecyclerView.ClickableRecyclerView;
import com.osh.apps.maps.clickableRecyclerView.adapter.PlaceAdapter;
import com.osh.apps.maps.place.SimplePlaceDetails;

import java.util.ArrayList;


public class FavoritesFragment extends Fragment
{
private ClickableRecyclerView recyclerView;
private PlaceAdapter adapter;


    public FavoritesFragment()
    {
    // Required empty public constructor

    }


    public static FavoritesFragment newInstance()
    {
    FavoritesFragment fragment=new FavoritesFragment();
    return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    ArrayList<SimplePlaceDetails> places=new ArrayList<>();
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Coffee", "jdkdk ,ljso 25", 3.5f));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Rest", "jdklnduis , jdjk ,msk sk 68128255545555", 4.0f));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Google", "jdkdk ,ljso 25", 5.0f));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Temp", "fghdfg jso 1235", 1.5f));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Test", "jfgd hdfdo 6225", 3));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Big Bang", "jdkdk dfgd 5", 0));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Pizza", "jdkdk ,ld ssa 723", 0.5f));

    adapter=new PlaceAdapter(getContext(), R.layout.rv_place_item);
    adapter.setPlaces(places);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    // Inflate the layout for this fragment
    View view=inflater.inflate(R.layout.fragment_list_places, container, false);

    recyclerView=(ClickableRecyclerView) view.findViewById(R.id.RecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);

    return view;
    }

}

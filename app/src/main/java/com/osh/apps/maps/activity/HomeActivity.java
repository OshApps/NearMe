package com.osh.apps.maps.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.osh.apps.maps.R;
import com.osh.apps.maps.clickableRecyclerView.ClickableRecyclerView;
import com.osh.apps.maps.clickableRecyclerView.adapter.PlaceAdapter;
import com.osh.apps.maps.place.SimplePlaceDetails;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity
{
private ClickableRecyclerView recyclerView;
private PlaceAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


    setViews();

    ArrayList<SimplePlaceDetails> places=new ArrayList<>();
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Coffee", "jdkdk ,ljso 25", 3.5f));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Rest", "jdklnduis , jdjk ,msk sk 68128255545555", 4.0f));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Google", "jdkdk ,ljso 25", 5.0f));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Temp", "fghdfg jso 1235", 1.5f));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Test", "jfgd hdfdo 6225", 3));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Big Bang", "jdkdk dfgd 5", 0));
    places.add(new SimplePlaceDetails(-1,"22fdgds55","Pizza", "jdkdk ,ld ssa 723", 0.5f));


    adapter.setPlaces(places);
    }


    private void setViews()
    {

    adapter=new PlaceAdapter(this, R.layout.rv_place_item);

    recyclerView=(ClickableRecyclerView) findViewById(R.id.RecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);
    }

}

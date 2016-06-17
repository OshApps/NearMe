package com.osh.apps.maps.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.osh.apps.maps.R;
import com.osh.apps.maps.adapter.PhotosAdapter;
import com.osh.apps.maps.app.AppData;
import com.osh.apps.maps.database.DatabaseManager;
import com.osh.apps.maps.network.connection.JsonConnection;
import com.osh.apps.maps.place.Place;
import com.osh.apps.maps.place.PlaceDetails;

import org.json.JSONArray;
import org.json.JSONObject;


public class DetailsFragment extends TabFragment
{
private static final int TITLE_RES=R.string.details_tab;

private static final String ARG_PLACE_ID="placeId";
private static final String ARG_PLACE_TYPE="placeType";

private TextView address,distance,phone,website;
private DatabaseManager databaseManager;
private PhotosAdapter photosAdapter;
private LinearLayout detailsLayout;
private ProgressBar loading;
private ViewPager viewPager;
private RatingBar rating;


    public DetailsFragment()
    {
    // Required empty public constructor
    }


    public static DetailsFragment newInstance(long placeId, int placeType)
    {
    DetailsFragment fragment;
    Bundle args;

    args=new Bundle();

    args.putLong(ARG_PLACE_ID, placeId);
    args.putInt(ARG_PLACE_TYPE, placeType);

    fragment=new DetailsFragment();
    fragment.setArguments(args);

    return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    databaseManager=DatabaseManager.getInstance(getContext());

    photosAdapter=new PhotosAdapter(getContext());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    View view=inflater.inflate(R.layout.fragment_place_details, container, false);

    viewPager=(ViewPager) view.findViewById(R.id.vp_photos);
    viewPager.setAdapter(photosAdapter);

    rating=(RatingBar) view.findViewById(R.id.rb_rating);

    detailsLayout=(LinearLayout) view.findViewById(R.id.details_layout);

    address=(TextView) view.findViewById(R.id.tv_address);
    distance=(TextView) view.findViewById(R.id.tv_distance);
    phone=(TextView) view.findViewById(R.id.tv_phone);
    website=(TextView) view.findViewById(R.id.tv_website);

    loading=(ProgressBar) view.findViewById(R.id.pb_loading);

    new PlaceDetailsTask().execute();

    return view;
    }


    public void showDetails()
    {
    detailsLayout.setVisibility(View.VISIBLE);

    //TODO animation

    /*
    for(int i=0 ; i<detailsLayout.getChildCount() ; i++)
        {
        detailsLayout.getChildAt(i).startAnimation(???);
        }
    */
    }


    @Override
    public int getTitleRes()
    {
    return TITLE_RES;
    }


    public class PlaceDetailsTask extends AsyncTask<Void, Void, PlaceDetails>
    {

        @Override
        protected PlaceDetails doInBackground(Void... params)
        {
        String apiKey,phone,website;
        PlaceDetails placeDetails;
        JSONObject json,result;
        String[] photoUrls;
        JSONArray photos;
        Bundle bundle;
        int placeType;
        long placeId;
        Place place;

        bundle=getArguments();

        placeId=bundle.getLong(ARG_PLACE_ID, AppData.NULL_DATA);
        placeType=bundle.getInt(ARG_PLACE_TYPE, AppData.NULL_DATA);

        placeDetails=null;
        place=null;

        if(placeId != AppData.NULL_DATA)
            {
            switch(placeType)
                {
                case AppData.Place.FAVOURITE_TYPE:
                place=databaseManager.getFavouritePlace(placeId);
                break;

                case AppData.Place.SEARCH_TYPE:
                place=databaseManager.getSearchPlace(placeId);
                break;
                }

            }

        if(place!=null)
            {
            apiKey=getString(R.string.places_api_key);

            try {
                json=new JSONObject( JsonConnection.getJsonResult( AppData.PlacesAPI.createDetailsUrl( place.getGoogleId(), apiKey ) ) );

                result=json.getJSONObject("result");

                photoUrls=null;

                try {
                    photos=result.getJSONArray("photos");

                    photoUrls=new String[photos.length()];

                    for(int i=0 ; i<photos.length() ; i++)
                        {
                        photoUrls[i]=AppData.PlacesAPI.createPhotoUrl(photos.getJSONObject(i).getString("photo_reference"), apiKey);
                        }

                    }catch(Exception e){}

                phone=null;
                website=null;

                try{
                   phone=result.getString("international_phone_number");
                   }catch(Exception e){}

                try{
                   website=result.getString("website");
                   }catch(Exception e){}

                placeDetails=new PlaceDetails(place, phone, website, photoUrls);
                }catch(Exception e)
                    {
                    e.printStackTrace();
                    }
            }

        return placeDetails;
        }


        @Override
        protected void onPostExecute(PlaceDetails placeDetails)
        {
        Place place;

        if(placeDetails!=null)
            {
            place=placeDetails.getPlace();

            photosAdapter.setImages(placeDetails.getPhotoUrls());

            rating.setRating(place.getRating());

            address.setText(place.getAddress());

            distance.setText("????");//TODO

            phone.setText(placeDetails.getPhone());

            website.setText(placeDetails.getWebsite());


            }


        loading.setVisibility(View.GONE);

        showDetails();
        }
    }

}

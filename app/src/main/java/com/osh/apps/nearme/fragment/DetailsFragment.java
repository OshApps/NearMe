package com.osh.apps.nearme.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.osh.apps.nearme.R;
import com.osh.apps.nearme.activity.callback.PlaceDetailsCallback;
import com.osh.apps.nearme.adapter.PhotosAdapter;
import com.osh.apps.nearme.app.AppData;
import com.osh.apps.nearme.database.DatabaseManager;
import com.osh.apps.nearme.network.connection.JsonConnection;
import com.osh.apps.nearme.place.Place;

import org.json.JSONArray;
import org.json.JSONObject;


public class DetailsFragment extends BaseFragment implements View.OnClickListener
{
private static final int TITLE_RES=R.string.title_tab_details;

private static final String ARG_PLACE_ID="placeId";

private TextView addressText,distanceText,phoneText,websiteText;
private PlaceDetailsCallback placeDetailsCallback;
private CardView distanceCard,phoneCard,websiteCard;
private PlaceDetailsTask placeDetailsTask;
private DatabaseManager databaseManager;
private PhotosAdapter photosAdapter;
private ViewPager viewPager;
private RatingBar rating;
private Place place;


    public DetailsFragment()
    {
    // Required empty public constructor
    }


    public static DetailsFragment newInstance(long placeId)
    {
    DetailsFragment fragment;
    Bundle args;

    args=new Bundle();

    args.putLong(ARG_PLACE_ID, placeId);

    fragment=new DetailsFragment();
    fragment.setArguments(args);

    return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    long placeId;

    placeDetailsTask=new PlaceDetailsTask();

    databaseManager=DatabaseManager.getInstance(getContext());

    placeId=getArguments().getLong(ARG_PLACE_ID, AppData.NULL_DATA);

    place=databaseManager.getPlace(placeId);

    photosAdapter=new PhotosAdapter(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    String phone,website;
    View view;

    view=inflater.inflate(R.layout.fragment_place_details, container, false);

    viewPager=(ViewPager) view.findViewById(R.id.vp_photos);
    viewPager.setOffscreenPageLimit(2);
    viewPager.setAdapter(photosAdapter);

    rating=(RatingBar) view.findViewById(R.id.rb_rating);

    addressText=(TextView) view.findViewById(R.id.tv_address);
    distanceText=(TextView) view.findViewById(R.id.tv_distance);
    phoneText=(TextView) view.findViewById(R.id.tv_phone);
    websiteText=(TextView) view.findViewById(R.id.tv_website);

    distanceCard=(CardView) view.findViewById(R.id.cv_distance);
    phoneCard=(CardView) view.findViewById(R.id.cv_phone);
    websiteCard=(CardView) view.findViewById(R.id.cv_website);

    view.findViewById(R.id.cv_address).setOnClickListener(this);

    phoneCard.setOnClickListener(this);
    websiteCard.setOnClickListener(this);
    addressText.setOnClickListener(this);
    distanceText.setOnClickListener(this);
    phoneText.setOnClickListener(this);
    websiteText.setOnClickListener(this);

    rating.setRating(place.getRating());

    addressText.setText(place.getAddress());

    phone=place.getPhone();
    website=place.getWebsite();

    if(phone!=null)
        {
        phoneText.setText(phone);
        phoneCard.setVisibility(View.VISIBLE);
        }

    if(website!=null)
        {
        websiteText.setText(website);
        websiteCard.setVisibility(View.VISIBLE);
        }

    updateDistance(placeDetailsCallback.getCurrentLocation());

    placeDetailsTask.execute();

    return view;
    }


    @Override
    public void onAttach(Context context)
    {
    super.onAttach(context);

    if(context instanceof PlaceDetailsCallback)
            {
            placeDetailsCallback=(PlaceDetailsCallback) context;
            }else
                {
                throw new RuntimeException(context.toString()+" must implement HomeActivityCallback");
                }
    }


    @Override
    public void onDetach()
    {
    super.onDetach();

    placeDetailsCallback=null;
    }


    @Override
    public void onDestroy()
    {
    super.onDestroy();

    photosAdapter.clearImages();
    place.removeIconImage();
    }


    private void updateDistance(Location location)
    {
    int distance;

    if(location != null)
        {
        place.updateDistance(location.getLatitude(),location.getLongitude());

        distance=place.getDistance();

        if(distance != AppData.NULL_DATA)
            {
            distanceText.setText(AppData.Preferences.getDistanceText(getContext(),distance));
            distanceCard.setVisibility(View.VISIBLE);
            }
        }

    }


    public void onLocationChanged(Location location)
    {

    if(isCreated())
        {
        updateDistance(location);
        }
    }


    @Override
    public void onClick(View v)
    {
    Intent intent;
    String text;

    switch(v.getId())
        {
        case R.id.tv_address:
        case R.id.cv_address:

        placeDetailsCallback.showMap();

        break;

        case R.id.tv_distance:
        case R.id.cv_distance:

        placeDetailsCallback.showMap();

        break;

        case R.id.tv_phone:
        case R.id.cv_phone:

        text=phoneText.getText().toString();

        if(text.length() > 0)
            {
            intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+text));
            startActivity(intent);
            }


        break;

        case R.id.tv_website:
        case R.id.cv_website:

        text=websiteText.getText().toString();

        if(text.length() > 0)
            {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(text));
            startActivity(intent);
            }
        break;
        }
    }


    public void showDetails()
    {
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


    public class PlaceDetailsTask extends AsyncTask<Void, Void, String[]>
    {

        @Override
        protected String[] doInBackground(Void... params)
        {
        JSONObject json,result;
        String[] photoUrls;
        JSONArray photos;
        String apiKey;

        photoUrls=null;

        apiKey=getString(R.string.places_api_key);

        try {
            json=new JSONObject( JsonConnection.getJsonResult( AppData.PlacesAPI.createDetailsUrl( place.getGoogleId(), apiKey ) ) );

            result=json.getJSONObject("result");



            try {
                photos=result.getJSONArray("photos");

                photoUrls=new String[photos.length()];

                for(int i=0 ; i<photos.length() ; i++)
                    {
                    photoUrls[i]=AppData.PlacesAPI.createPhotoUrl(photos.getJSONObject(i).getString("photo_reference"), apiKey);
                    }

                }catch(Exception e){}

            }catch(Exception e)
                {
                e.printStackTrace();
                }


        return photoUrls;
        }


        @Override
        protected void onPostExecute(String[] photoUrls)
        {
        photosAdapter.setImages(photoUrls);
        }
    }

}

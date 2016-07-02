package com.osh.apps.nearme.activity.callback;

import android.support.v4.app.Fragment;

import com.osh.apps.nearme.place.Place;


/**
 * Created by oshri-n on 23/06/2016.
 */
public interface HomeActivityCallback extends LocationCallback
{
void openPlaceDetailsActivity(long placeId);
void onRemoveFavouritePlace(Fragment fragment, long placeId);
void onAddFavouritePlace(Place place);
}

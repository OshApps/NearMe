package com.osh.apps.maps;

import com.osh.apps.maps.place.Place;


/**
 * Created by oshri-n on 23/06/2016.
 */
public interface HomeActivityCallback
{
void openPlaceDetailsActivity(long placeId, String name);
void onRemoveFavouritePlace(int fragmentId, long placeId);
void onAddFavouritePlace(Place place);

}

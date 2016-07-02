package com.osh.apps.nearme.activity.callback;

import com.osh.apps.nearme.fragment.BaseFragment;
import com.osh.apps.nearme.place.Place;


/**
 * Created by oshri-n on 23/06/2016.
 */
public interface HomeActivityCallback extends LocationCallback
{
void openPlaceDetailsActivity(long placeId);
void onRemoveFavouritePlace(BaseFragment fragment, long placeId);
void onAddFavouritePlace(Place place);

}

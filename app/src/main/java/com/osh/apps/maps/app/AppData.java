package com.osh.apps.maps.app;

/**
 * Created by oshri-n on 16/05/2016.
 */
public final class AppData
{
public static final int NULL_DATA=-1;


	public static final class PlacesAPI
    {
    private static final String BASE_URL="https://maps.googleapis.com/maps/api/place/";

    private static final int PHOTO_MAX_SIZE=1600;

    private static final String SEARCH_REQUEST="nearbysearch/json?";
    private static final String DETAILS_REQUEST="details/json?";
    private static final String PHOTO_REQUEST="photo?";

    private static final String PLACE_ID_FIRST_PARAMETER="placeid=";
    private static final String LOCATION_FIRST_PARAMETER="location=";
    private static final String MAX_WIDTH_FIRST_PARAMETER="maxwidth=";
    private static final String MAX_HEIGHT_FIRST_PARAMETER="maxheight=";

    private static final String RADIUS_PARAMETER="&radius=";
    private static final String KEYWORD_PARAMETER="&keyword=";
    private static final String KEY_PARAMETER="&key=";
    private static final String PHOTO_REFERENCE_PARAMETER="&photoreference=";


        private static String getKeywordFormat(String keyword)
        {
        return keyword.trim().replaceAll("\\s","%20");
        }


        public static String createSearchUrl(String keyword, double lat, double lng, int radius, String apiKey)
        {
        String url=BASE_URL;

        url+= SEARCH_REQUEST;
        url+= LOCATION_FIRST_PARAMETER + lat + "," + lng;
        url+= RADIUS_PARAMETER + radius;

        if(keyword != null)
            {
            url+=KEYWORD_PARAMETER + getKeywordFormat(keyword);
            }

        url+=KEY_PARAMETER + apiKey;

        return url;
        }


        public static String createDetailsUrl(String googleId, String apiKey)
        {
        String url=BASE_URL;

        url+= DETAILS_REQUEST;
        url+= PLACE_ID_FIRST_PARAMETER + googleId;
        url+= KEY_PARAMETER + apiKey;

        return url;
        }


        public static String createPhotoUrl(String photoReference, String apiKey)
        {
        return createPhotoUrl(photoReference, PHOTO_MAX_SIZE, PHOTO_MAX_SIZE, apiKey);
        }


        public static String createPhotoUrl(String photoReference, int width, int height, String apiKey)
        {
        String url=BASE_URL;

        url+= PHOTO_REQUEST;

        width= (width==NULL_DATA)? PHOTO_MAX_SIZE : width;
        height= (height==NULL_DATA)? PHOTO_MAX_SIZE : height;

        url+= MAX_WIDTH_FIRST_PARAMETER + width + "&" +MAX_HEIGHT_FIRST_PARAMETER + height;


        url+= PHOTO_REFERENCE_PARAMETER + photoReference;
        url+= KEY_PARAMETER + apiKey;

        return url;
        }

    }


}

package com.osh.apps.maps.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.osh.apps.maps.network.connection.HttpsConnection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by oshri-n on 14/06/2016.
 */
public class ImageLoaderManager
{
private static ArrayList<ImageRequest> requests;
private static boolean isRunning;
private static Handler handler;
private static Runnable action;


    private static void init()
    {
    requests=new ArrayList<>();
    isRunning=false;
    handler=new Handler();

    action=new Runnable()
        {
            @Override
            public void run()
            {
            onBackground();
            }
        };
    }


    public static void loadImage(ImageData image, ImageView imageView, ProgressBar loading)
    {
    loadImage(image,imageView, loading, imageView.getWidth(), imageView.getHeight());
    }


    public static void loadImage(ImageData image, ImageView imageView, ProgressBar loading, int width, int height)
    {
    addRequest(new ImageRequest(image, imageView, loading, width, height));
    }


    static synchronized void addRequest(ImageRequest request)
    {

    if(requests==null)
        {
        init();
        }

    if(!request.isDone())
        {
        request.preLoad();
        requests.add(request);

        if(!isRunning)
            {
            isRunning=true;
            new Thread(action, "load images task").start();
            }
        }
    }


    private static void onBackground()
    {
    ImageRequest request;
    Bitmap bitmap;
    String url;

    while(!requests.isEmpty())
        {
        request=requests.remove(0);

        if(request.isDone())
            {
            continue;
            }

        url=request.getUrl();
        bitmap=null;

        if(url!=null)
            {
            if(url.startsWith("http"))
                {
                bitmap=getBitmapFromWeb(url, request.getWidth(), request.getHeight());
                }
            }

        postResult(request, bitmap);

        }

    isRunning=false;
    }


    private static void postResult(final ImageRequest request, final Bitmap bitmap)
    {
    Log.d("ImageLoaderManager","post image");
    handler.post(new Runnable()
        {

            @Override
            public void run()
            {
            request.onLoad(bitmap);
            }
        });
    }


    private static Bitmap getBitmapFromWeb(String url, int width, int height)
    {
    BitmapFactory.Options bitmapOptions;
    InputStream inputStream = null;
    Bitmap bitmap = null;

    try {
        inputStream= HttpsConnection.getInputStream(url);

        bitmapOptions=getBitmapOptions(inputStream, width ,height);

        inputStream=HttpsConnection.getInputStream(url);

        bitmap= BitmapFactory.decodeStream(inputStream, null ,bitmapOptions);

        if(bitmap!=null)
            {
            bitmap=Bitmap.createScaledBitmap(bitmap, width, height, true);
            }

        } catch (Exception e)
            {
            Log.e("ImageLoaderManager",""+e.getMessage());
            }

    finally
        {
        if(inputStream!=null)
            {
            try {
                inputStream.close();
                } catch(Exception e) {}
            }
        }

    return bitmap;
    }


    private static BitmapFactory.Options getBitmapOptions(InputStream inputStream, int width, int height) throws IOException
    {
    BitmapFactory.Options options= createBitmapOptions();

    BitmapFactory.decodeStream(inputStream, null ,options);

    scaleBitmap(options, width, height);

    inputStream.close();

    return options;
    }


    private static BitmapFactory.Options createBitmapOptions()
    {
    BitmapFactory.Options options= new BitmapFactory.Options();

    options.inJustDecodeBounds = true;

    return options;
    }


    private static void scaleBitmap(BitmapFactory.Options options, int width, int height)
    {
    options.inJustDecodeBounds = false;

    options.inSampleSize = Math.min(options.outWidth / width, options.outHeight / height);
    }

}

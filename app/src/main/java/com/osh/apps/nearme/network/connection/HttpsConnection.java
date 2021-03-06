package com.osh.apps.nearme.network.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by oshri-n on 24/05/2016.
 */
public class HttpsConnection
{
private static final int DEFAULT_CONNECTION_TIMEOUT = 3000;


    private static HttpURLConnection getConnection(String webUrl, int connectionTimeout) throws IOException
    {
    URL url=new URL(webUrl);

    HttpsURLConnection connection= (HttpsURLConnection) url.openConnection();
    connection.setConnectTimeout(connectionTimeout);
    connection.setReadTimeout(connectionTimeout);

    if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
        throw new IOException("Connection failed - "+connection.getResponseCode());
        }

    return connection;
    }


    public static InputStream getInputStream(String url, int connectionTimeout) throws IOException
    {
    return getConnection(url, connectionTimeout).getInputStream();
    }

    public static InputStream getInputStream(String url) throws IOException
    {
    return getInputStream(url,DEFAULT_CONNECTION_TIMEOUT);
    }

}

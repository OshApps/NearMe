package com.osh.apps.nearme.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * Created by oshri-n on 07/07/2016.
 */
public class PowerReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
    String action=intent.getAction();

    if(action.equals(Intent.ACTION_POWER_CONNECTED))
        {
        Toast.makeText(context, "power connected", Toast.LENGTH_SHORT).show();
        }else if(action.equals(Intent.ACTION_POWER_DISCONNECTED))
            {
            Toast.makeText(context, "power disconnected", Toast.LENGTH_SHORT).show();
            }
    }

}

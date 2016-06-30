package com.osh.apps.maps.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.osh.apps.maps.fragment.BaseFragment;


/**
 * Created by oshri-n on 09/06/2016.
 */
public class FragmentsAdapter extends FragmentPagerAdapter
{
private BaseFragment[] fragments;
private Context context;


    public FragmentsAdapter(Context context, FragmentManager fm, BaseFragment... fragments )
    {
    super(fm);

    this.context=context;
    this.fragments=fragments;

    }


    @Override
    public Fragment getItem(int position)
    {
    return fragments[position];
    }


    @Override
    public int getCount()
    {
    return fragments.length;
    }


    @Override
    public CharSequence getPageTitle(int position)
    {
    return context.getString(fragments[position].getTitleRes());
    }


    public int getItemPosition(BaseFragment fragment)
    {
    int itemPosition=POSITION_NONE;

    for(int i=0 ; i<fragments.length ; i++)
        {

        if(fragment == fragments[i])
            {
            itemPosition=i;
            break;
            }
        }

    return itemPosition;
    }

}

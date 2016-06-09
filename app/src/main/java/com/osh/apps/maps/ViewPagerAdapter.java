package com.osh.apps.maps;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by oshri-n on 09/06/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter
{
private FragmentItem[] fragments;


    public ViewPagerAdapter(FragmentManager fm, FragmentItem... fragments )
    {
    super(fm);

    this.fragments=fragments;
    }


    @Override
    public Fragment getItem(int position)
    {
    return fragments[position].getFragment();
    }


    @Override
    public int getCount()
    {
    return fragments.length;
    }


    @Override
    public CharSequence getPageTitle(int position)
    {
    return fragments[position].getTitle();
    }


    public static class FragmentItem
    {
    private Fragment fragment;
    private String title;


        public FragmentItem(Fragment fragment, String title)
        {
        this.fragment=fragment;
        this.title=title;
        }


        public Fragment getFragment()
        {
        return fragment;
        }


        public String getTitle()
        {
        return title;
        }
    }

}

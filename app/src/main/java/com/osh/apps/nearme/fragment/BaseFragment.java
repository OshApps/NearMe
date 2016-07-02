package com.osh.apps.nearme.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;


/**
 * Created by oshri-n on 15/06/2016.
 */
public abstract class BaseFragment extends Fragment
{
private boolean isCreated;

abstract public int getTitleRes();


    public BaseFragment()
    {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    isCreated=false;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
    super.onViewCreated(view, savedInstanceState);

    isCreated=true;
    }


    public boolean isCreated()
    {
    return isCreated;
    }
}

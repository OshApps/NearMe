package com.osh.apps.maps.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.osh.apps.maps.R;
import com.osh.apps.maps.adapter.FragmentsAdapter;
import com.osh.apps.maps.fragment.FavouritesFragment;
import com.osh.apps.maps.fragment.SearchFragment;


public class HomeActivity extends AppCompatActivity
{
private FavouritesFragment favouritesFragment;
private FragmentsAdapter fragmentsAdapter;
private SearchFragment searchFragment;
private int searchFragmentPosition;
private SearchView searchView;
private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_home);

    init();

    setViews();
    }


    private void init()
    {
    searchFragment=SearchFragment.newInstance();
    favouritesFragment=FavouritesFragment.newInstance();

    fragmentsAdapter=new FragmentsAdapter( this , getSupportFragmentManager(), searchFragment, favouritesFragment );

    searchFragmentPosition=fragmentsAdapter.getItemPosition(searchFragment);

    }


    private void setViews()
    {
    Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            searchFragment.onSearch(null);

            viewPager.setCurrentItem(searchFragmentPosition,true);
            }
        });

    viewPager=(ViewPager) findViewById(R.id.ViewPager);
    viewPager.setAdapter(fragmentsAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_tabs);
    tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    getMenuInflater().inflate( R.menu.menu_home_activity, menu);

    MenuItem myActionMenuItem = menu.findItem( R.id.m_search);

    searchView = (SearchView) myActionMenuItem.getActionView();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
            hideKeyboard();

            searchFragment.onSearch(query);

            viewPager.setCurrentItem(searchFragmentPosition, true);
            return true;
            }


            @Override
            public boolean onQueryTextChange(String s)
            {
            return false;
            }

        });

    return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    boolean isActionDone=true;

    switch(item.getItemId())
        {
        case R.id.m_clear_history:
        //TODO
        break;

        case R.id.m_setting:
        //TODO
        break;

        default:
        isActionDone=false;
        }

    return isActionDone;
    }


    private void hideKeyboard()
    {
    View view = getCurrentFocus();

    if (view != null)
        {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

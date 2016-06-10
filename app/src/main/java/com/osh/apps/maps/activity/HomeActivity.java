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
import android.widget.Toast;

import com.osh.apps.maps.R;
import com.osh.apps.maps.service.SearchService;
import com.osh.apps.maps.ViewPagerAdapter;
import com.osh.apps.maps.fragment.FavoritesFragment;
import com.osh.apps.maps.fragment.PlacesFragment;


public class HomeActivity extends AppCompatActivity
{
private FavoritesFragment favoritesFragment;
private ViewPagerAdapter viewPagerAdapter;
private int placesFragmentPosition;
private PlacesFragment placesFragment;
private SearchView searchView;
private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

    ViewPagerAdapter.FragmentItem[] fragmentItems;

    setContentView(R.layout.activity_home);
    Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            viewPager.setCurrentItem(placesFragmentPosition,true);
            }
        });

    favoritesFragment=FavoritesFragment.newInstance();
    placesFragment=PlacesFragment.newInstance();

    fragmentItems=new ViewPagerAdapter.FragmentItem[]{new ViewPagerAdapter.FragmentItem( placesFragment, getString(R.string.places_tab) ),
                                                      new ViewPagerAdapter.FragmentItem( favoritesFragment, getString(R.string.favorites_tab) )
                                                      };
    placesFragmentPosition=0;

    viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), (ViewPagerAdapter.FragmentItem[]) fragmentItems);

    viewPager=(ViewPager) findViewById(R.id.ViewPager);
    viewPager.setAdapter(viewPagerAdapter);

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

            Toast.makeText(HomeActivity.this, query, Toast.LENGTH_SHORT).show();

            hideKeyboard();
            viewPager.setCurrentItem(placesFragmentPosition,true);
            SearchService.startActionSearch(getBaseContext() , query);

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

package com.allan.consumerapp.view;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.allan.consumerapp.R;
import com.allan.consumerapp.adapter.FragmentAdapter;
import com.allan.consumerapp.view.fragment.FavMovieFragment;
import com.allan.consumerapp.view.fragment.FavTvFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigation;
    private FragmentAdapter fragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupPageAdapter();
        setupViewPager();
        setupBottomNavigation();
        setToolbarTitle(getString(R.string.app_name));
    }

    private void setupPageAdapter() {
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addPage(new FavMovieFragment(), getString(R.string.tab_movie));
        fragmentAdapter.addPage(new FavTvFragment(), getString(R.string.tab_tv_show));
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.view_pager_container);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    private void setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    private void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_movie:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_tv:
                viewPager.setCurrentItem(1);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setToolbarTitle(fragmentAdapter.getPageTitle(position));
        if (position == 0) {
            bottomNavigation.setSelectedItemId(R.id.navigation_movie);
        } else if (position == 1) {
            bottomNavigation.setSelectedItemId(R.id.navigation_tv);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


}

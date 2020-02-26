package com.allan.moviecatalogueuiux.view.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.allan.moviecatalogueuiux.R;
import com.allan.moviecatalogueuiux.adapter.FragmentAdapter;
import com.allan.moviecatalogueuiux.view.activity.setting.SettingActivity;
import com.allan.moviecatalogueuiux.view.fragment.favorite.FavoriteFragment;
import com.allan.moviecatalogueuiux.view.fragment.movie.MovieFragment;
import com.allan.moviecatalogueuiux.view.fragment.tvshow.TvShowsFragment;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.change_languange) {
            changeLanguage();
        }else if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeLanguage() {
        Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(mIntent);
    }

    private void setupPageAdapter() {
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addPage(new MovieFragment(), getString(R.string.tab_movie));
        fragmentAdapter.addPage(new TvShowsFragment(), getString(R.string.tab_tv_show));
        fragmentAdapter.addPage(new FavoriteFragment(), getString(R.string.tab_fav));
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
            case R.id.navigation_fav:
                viewPager.setCurrentItem(2);
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
        }else if(position == 1){
            bottomNavigation.setSelectedItemId(R.id.navigation_tv);
        }else {
            bottomNavigation.setSelectedItemId(R.id.navigation_fav);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}

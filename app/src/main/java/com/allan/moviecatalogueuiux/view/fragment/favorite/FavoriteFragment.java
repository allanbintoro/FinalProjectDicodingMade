package com.allan.moviecatalogueuiux.view.fragment.favorite;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.allan.moviecatalogueuiux.R;
import com.allan.moviecatalogueuiux.adapter.FavoritePagerAdapter;
import com.allan.moviecatalogueuiux.view.fragment.favMovie.FavoriteMovieFragment;
import com.allan.moviecatalogueuiux.view.fragment.favTv.FavoriteTvFragment;
import com.google.android.material.tabs.TabLayout;


public class FavoriteFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        FavoritePagerAdapter adapter = new FavoritePagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FavoriteMovieFragment(), getString(R.string.tab_movie));
        adapter.addFragment(new FavoriteTvFragment(), getString(R.string.tab_tv_show));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}

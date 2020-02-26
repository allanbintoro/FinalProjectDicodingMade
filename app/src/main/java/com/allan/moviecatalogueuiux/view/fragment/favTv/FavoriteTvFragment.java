package com.allan.moviecatalogueuiux.view.fragment.favTv;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.moviecatalogueuiux.R;
import com.allan.moviecatalogueuiux.adapter.MovieAdapter;
import com.allan.moviecatalogueuiux.data.db.RoomClient;
import com.allan.moviecatalogueuiux.data.model.Movie;
import com.allan.moviecatalogueuiux.interfaces.ItemMovieListener;
import com.allan.moviecatalogueuiux.view.activity.detail.MovieDetailActivity;
import com.allan.moviecatalogueuiux.viewmodel.FavoriteViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

public class FavoriteTvFragment extends Fragment implements ItemMovieListener {

    private TextView tvNoFav;
    private RecyclerView rcMovie;
    private MovieAdapter movieAdapter;
    private FavoriteViewModel favoriteViewModel;
    private GetFavoriteTask getFavoriteTask;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        bindView(view);
        setupTvRecyclerView();
        registerObserver();
        getFavoriteTask.execute();
    }

    private void initialize() {
        initViewModel();
        initFavoriteTask();
    }

    private void initFavoriteTask() {
        getFavoriteTask = new GetFavoriteTask(this);
    }

    private void initViewModel() {
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
    }

    private void bindView(View view) {
        tvNoFav = view.findViewById(R.id.tv_empty);
        rcMovie = view.findViewById(R.id.rv_movie);
    }

    private void setupTvRecyclerView() {
        movieAdapter = new MovieAdapter(this);
        rcMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rcMovie.setAdapter(movieAdapter);
    }


    private void registerObserver() {
        favoriteViewModel.getMovieList().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                tvNoFav.setVisibility(movies.isEmpty() ? View.VISIBLE : View.GONE);
                movieAdapter.setMovieList(movies);
                movieAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onMovieClicked(Movie results) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, results);
            getActivity().startActivity(intent);
        }
    }

    public static class GetFavoriteTask extends AsyncTask<Void, Void, List<Movie>> {
        private WeakReference<FavoriteTvFragment> fragment;

        GetFavoriteTask(FavoriteTvFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return RoomClient.getInstance(fragment.get().getContext())
                    .getMovieDatabase()
                    .getMovieDao()
                    .getMovieList("tv");
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            fragment.get().favoriteViewModel.setMovieList(movies);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getFavoriteTask = null;
        initFavoriteTask();
        getFavoriteTask.execute();
    }
}

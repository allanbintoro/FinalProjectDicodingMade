package com.allan.consumerapp.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
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

import com.allan.consumerapp.R;
import com.allan.consumerapp.adapter.MovieAdapter;
import com.allan.consumerapp.db.DataObserver;
import com.allan.consumerapp.db.DatabaseContract;
import com.allan.consumerapp.db.MappingHelper;
import com.allan.consumerapp.interfaces.LoadFavoriteMovieListener;
import com.allan.consumerapp.model.Movie;
import com.allan.consumerapp.task.GetFavoriteTask;
import com.allan.consumerapp.view.MainViewModel;

import java.util.List;

public class FavTvFragment extends Fragment implements LoadFavoriteMovieListener {

    private static final String KEY_TV = "tv";
    private RecyclerView rv_movie;
    private TextView tv_no_movie;
    private MovieAdapter movieAdapter;
    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        bindView(view);
        setupRecyclerView();
        observeMovieList();
        registerContentObserver();
        getFavTv();
    }

    public void getFavTv() {
        new GetFavoriteTask(getContext(), this).execute();
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void bindView(View view) {
        rv_movie = view.findViewById(R.id.rv_movie);
        tv_no_movie = view.findViewById(R.id.tv_no_movie);
    }

    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter();
        rv_movie.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_movie.setAdapter(movieAdapter);
    }


    private void registerContentObserver() {
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        Log.e("test:", String.valueOf(DatabaseContract.AppDatabase.CONTENT_URI));

        getContext().getContentResolver().registerContentObserver(
                DatabaseContract.AppDatabase.CONTENT_URI,
                true,
                new DataObserver.Observer(handler, getContext()));
    }

    private void observeMovieList() {
        mainViewModel.getMovie().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieAdapter.setMoviesList(MappingHelper.filterfav(movies, KEY_TV));
                if (movieAdapter.getItemCount() == 0) {
                    tv_no_movie.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onFavoriteMovieLoaded(List<Movie> movies) {
        mainViewModel.setMovie(movies);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFavTv();
    }
}

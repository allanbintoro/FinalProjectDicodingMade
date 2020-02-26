package com.allan.moviecatalogueuiux.view.fragment.tvshow;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.moviecatalogueuiux.R;
import com.allan.moviecatalogueuiux.adapter.MovieAdapter;
import com.allan.moviecatalogueuiux.data.model.Movie;
import com.allan.moviecatalogueuiux.interfaces.ItemMovieListener;
import com.allan.moviecatalogueuiux.view.activity.detail.MovieDetailActivity;
import com.allan.moviecatalogueuiux.viewmodel.MovieViewModel;

import java.util.List;

public class TvShowsFragment extends Fragment implements ItemMovieListener {

    private EditText edtSearch;
    private RecyclerView rvTvShow;
    private ProgressBar progressBar;
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_shows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        initViewModel();
        setupSearchListener();
        registerObserver();
        initMovieRcv();
        movieViewModel.getTvShow();
    }

    private void bindView(View view) {
        edtSearch = view.findViewById(R.id.edt_search);
        rvTvShow = view.findViewById(R.id.rv_tv);
        progressBar = view.findViewById(R.id.progress_loading);
    }

    private void initViewModel() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
    }

    private void setupSearchListener() {
        edtSearch.setVisibility(View.VISIBLE);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                String query = text.toString();
                if (query.trim().length() > 3) {
                    movieViewModel.fetchSearchTv(query);
                } else if (query.trim().length() == 0) {
                    movieViewModel.getTvShow();
                }
            }
        });
    }

    private void initMovieRcv() {
        movieAdapter = new MovieAdapter(this);
        rvTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTvShow.setAdapter(movieAdapter);
    }

    @Override
    public void onMovieClicked(Movie results) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, results);
            getActivity().startActivity(intent);
        }
    }

    private void registerObserver() {
        observeIsLoading();
        observeMessage();
        observerMovieList();
    }

    private void observeIsLoading() {
        movieViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                handleLoading(isLoading);
            }
        });
    }

    private void observeMessage() {
        movieViewModel.getMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void observerMovieList() {
        movieViewModel.getMovieList().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> results) {
                movieAdapter.setMovieList(results);
                movieAdapter.notifyDataSetChanged();
            }
        });
    }

    private void handleLoading(Boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }


}

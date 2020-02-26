package com.allan.moviecatalogueuiux.viewmodel;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allan.moviecatalogueuiux.data.model.Movie;
import com.allan.moviecatalogueuiux.view.activity.detail.MovieDetailActivity;

public class MovieDetailViewModel extends ViewModel {
    private MutableLiveData<Movie> movie = new MutableLiveData<>();

    public void processIntent(Intent intent) {
        if (intent.hasExtra(MovieDetailActivity.EXTRA_MOVIE)) {
            Movie movie = intent.getParcelableExtra(MovieDetailActivity.EXTRA_MOVIE);
            this.movie.postValue(movie);
        }
    }

    public void setIsFavorite(Boolean isFavorite) {
        if (movie.getValue() != null) {
            Movie movieModel = movie.getValue();
            movieModel.setFavorite(isFavorite);
            movie.postValue(movieModel);
        }
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}

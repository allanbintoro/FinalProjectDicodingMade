package com.allan.moviecatalogueuiux.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allan.moviecatalogueuiux.data.model.Movie;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private MutableLiveData<List<Movie>> movie = new MutableLiveData<>();

    public LiveData<List<Movie>> getMovieList() {
        return movie;
    }

    public void setMovieList(List<Movie> movies) {
        this.movie.postValue(movies);
    }
}

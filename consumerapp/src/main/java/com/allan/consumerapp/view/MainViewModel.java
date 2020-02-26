package com.allan.consumerapp.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allan.consumerapp.model.Movie;

import java.util.List;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();

    public void setMovie(List<Movie> movie) {
        movies.postValue(movie);
    }

    public LiveData<List<Movie>> getMovie() {
        return movies;
    }
}

package com.allan.consumerapp.interfaces;

import com.allan.consumerapp.model.Movie;

import java.util.List;

public interface LoadFavoriteMovieListener {

    void onFavoriteMovieLoaded(List<Movie> movies);
}

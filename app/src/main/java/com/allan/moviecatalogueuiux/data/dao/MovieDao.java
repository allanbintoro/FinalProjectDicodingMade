package com.allan.moviecatalogueuiux.data.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.allan.moviecatalogueuiux.data.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie WHERE type=:type")
    List<Movie> getMovieList(String type);

    @Insert
    void addFavorite(Movie movie);

    @Query("SELECT * FROM movie WHERE id=:id")
    Movie getMovie(int id);

    @Query("DELETE FROM movie WHERE id=:id")
    void removeFavorite(Integer id);

    @Query("SELECT * FROM movie")
    Cursor getMovieCursor();
}

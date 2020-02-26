package com.allan.moviecatalogueuiux.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.allan.moviecatalogueuiux.data.dao.MovieDao;
import com.allan.moviecatalogueuiux.data.model.Movie;

@Database(entities = Movie.class, exportSchema = false, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao getMovieDao();

}

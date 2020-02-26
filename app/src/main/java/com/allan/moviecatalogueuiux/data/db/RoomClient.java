package com.allan.moviecatalogueuiux.data.db;

import android.content.Context;

import androidx.room.Room;

public class RoomClient {

    private static final String DB_NAME = "movie_db";
    private static RoomClient INSTANCE;
    private MovieDatabase movieDatabase;

    private RoomClient(Context context) {
        movieDatabase = Room.databaseBuilder(
                context.getApplicationContext(),
                MovieDatabase.class,
                DB_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public static synchronized RoomClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RoomClient(context);
        }
        return INSTANCE;
    }

    public MovieDatabase getMovieDatabase() {
        return movieDatabase;
    }
}

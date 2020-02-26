package com.allan.consumerapp.db;

import android.database.Cursor;
import android.util.Log;

import com.allan.consumerapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MappingHelper {

    public static ArrayList<Movie> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            String movie_title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.AppDatabase.MOVIE_TITLE));
            String movie_original_name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.AppDatabase.MOVIE_ORIGINAL_NAME));
            String movie_backdrop_image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.AppDatabase.MOVIE_BACKDROP_IMAGE));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.AppDatabase.MOVIE_TYPE));
            movies.add(new Movie(movie_title,movie_original_name, movie_backdrop_image, type));
        }
        Log.e("mapCursorToArrayList: ", movies+"" );
        return movies;
    }

    public static ArrayList<Movie> filterfav(List<Movie> movies, String movie_type) {
        ArrayList<Movie> filteredFavMovie = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getType().equals(movie_type)) {
                filteredFavMovie.add(movies.get(i));
            }
        }
        Log.e("filterfav: ",filteredFavMovie.toString()+"" );
        return filteredFavMovie;
    }
}

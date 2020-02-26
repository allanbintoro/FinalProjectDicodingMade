package com.allan.consumerapp.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.allan.consumerapp.db.DatabaseContract;
import com.allan.consumerapp.db.MappingHelper;
import com.allan.consumerapp.interfaces.LoadFavoriteMovieListener;
import com.allan.consumerapp.model.Movie;

import java.lang.ref.WeakReference;
import java.util.List;

public class GetFavoriteTask extends AsyncTask<Void, Void, List<Movie>> {

    private WeakReference<Context> contextReference;
    private LoadFavoriteMovieListener loadFavoriteMovieListener;

    public GetFavoriteTask(Context context, LoadFavoriteMovieListener loadFavoriteMovieListener) {
        contextReference = new WeakReference<>(context);
        this.loadFavoriteMovieListener = loadFavoriteMovieListener;
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {
        Cursor movie_cursor = contextReference.get()
                .getApplicationContext()
                .getContentResolver()
                .query(DatabaseContract.AppDatabase.CONTENT_URI, null, null, null, null);
        return MappingHelper.mapCursorToArrayList(movie_cursor);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        loadFavoriteMovieListener.onFavoriteMovieLoaded(movies);
    }
}

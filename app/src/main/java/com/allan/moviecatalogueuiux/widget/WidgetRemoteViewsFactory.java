package com.allan.moviecatalogueuiux.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.allan.moviecatalogueuiux.R;
import com.allan.moviecatalogueuiux.data.db.RoomClient;
import com.allan.moviecatalogueuiux.data.model.Movie;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private List<Movie> movies = new ArrayList<>();

    WidgetRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        movies = RoomClient.getInstance(context)
                .getMovieDatabase()
                .getMovieDao()
                .getMovieList("movie");
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_movie_widget);

        if (!movies.isEmpty()) {
            Movie movie = movies.get(position);
            try {
                Bitmap imageBitmap = Glide.with(context)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w185/"+movie.getBackdropPath())
                        .submit()
                        .get();
                remoteViews.setImageViewBitmap(R.id.img_movie_widget, imageBitmap);
                remoteViews.setTextViewText(R.id.tv_movie_title, movie.getOriginalTitle());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent();
            intent.putExtra(MovieWidget.EXTRA_MOVIE, movie);
            remoteViews.setOnClickFillInIntent(R.id.container_view_widget, intent);
        }

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

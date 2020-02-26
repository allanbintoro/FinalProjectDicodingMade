package com.allan.moviecatalogueuiux.view.activity.detail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.allan.moviecatalogueuiux.R;
import com.allan.moviecatalogueuiux.data.db.RoomClient;
import com.allan.moviecatalogueuiux.data.model.Movie;
import com.allan.moviecatalogueuiux.viewmodel.MovieDetailViewModel;
import com.allan.moviecatalogueuiux.widget.MovieWidget;
import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    private ImageView moviePoster;
    private TextView tvMovieTitle, tvMovieOverview;
    private MovieDetailViewModel detailViewModel;
    private Button favoriteButton;
    private InsertFavoriteTask insertFavoriteTask;
    private RemoveFavoriteTask removeFavoriteTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        bindView();
        setupToolbar();
        setupViewModel();
        registerObserver();
        detailViewModel.processIntent(getIntent());
    }

    private void bindView() {
        moviePoster = findViewById(R.id.img_movie);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieOverview = findViewById(R.id.tvMovieOverview);
        favoriteButton = findViewById(R.id.btn_favorite);
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.movie_detail));
            getSupportActionBar().setElevation(0f);
        }
    }

    private void setupViewModel() {
        detailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
    }

    private void registerObserver() {
        detailViewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                initFavoriteTask();
                checkFavoriteMovie(movie);
                setDetailView(movie);
                setFavoriteButton(movie.getFavorite());
            }
        });
    }

    private void setDetailView(Movie movie) {
        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w780" + movie.getBackdropPath())
                .into(moviePoster);
        tvMovieTitle.setText(movie.getTitle() != null ? movie.getTitle() : movie.getOriginalName());
        tvMovieOverview.setText(movie.getOverview().equalsIgnoreCase("") ? getResources().getString(R.string.movie_synopsis_empty) : movie.getOverview());
        favoriteButton.setOnClickListener(getFavoriteListener());
    }

    private View.OnClickListener getFavoriteListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isFavorite = Objects.requireNonNull(detailViewModel.getMovie().getValue()).getFavorite();
                if (isFavorite != null) {
                    handleFavoriteClicked(isFavorite);
                }
            }
        };
    }

    private void handleFavoriteClicked(Boolean isFavorite) {
        detailViewModel.setIsFavorite(isFavorite);
        if (isFavorite) {
            removeFavoriteTask.execute();
        } else {
            insertFavoriteTask.execute();
        }
    }

    private void checkFavoriteMovie(Movie movie) {
        new GetFavoriteTask(this, movie.getId()).execute();
    }

    private void setFavoriteButton(Boolean isFavorite) {
        favoriteButton.setText(getString(isFavorite ? R.string.action_remove_favorite : R.string.action_add_to_favorite));
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initFavoriteTask() {
        int movieId = Objects.requireNonNull(detailViewModel.getMovie().getValue()).getId();
        insertFavoriteTask = new InsertFavoriteTask(this);
        removeFavoriteTask = new RemoveFavoriteTask(this, movieId);
    }

    private static class GetFavoriteTask extends AsyncTask<Void, Void, Movie> {
        private int movieId;
        private WeakReference<MovieDetailActivity> activity;

        GetFavoriteTask(MovieDetailActivity activity, int movieId) {
            this.activity = new WeakReference<>(activity);
            this.movieId = movieId;
        }

        @Override
        protected Movie doInBackground(Void... voids) {
            return RoomClient.getInstance(activity.get())
                    .getMovieDatabase()
                    .getMovieDao()
                    .getMovie(movieId);
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            if (movie != null) {
                activity.get().detailViewModel.setIsFavorite(movie.getFavorite());
            } else {
                activity.get().detailViewModel.setIsFavorite(false);
            }
        }
    }

    private static class InsertFavoriteTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<MovieDetailActivity> activity;

        private InsertFavoriteTask(MovieDetailActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Movie movie = activity.get().detailViewModel.getMovie().getValue();
            if (movie != null) {
                if (movie.getTitle() != null) {
                    movie.setFavorite(true);
                    movie.setType("movie");
                    RoomClient.getInstance(activity.get())
                            .getMovieDatabase()
                            .getMovieDao()
                            .addFavorite(movie);
                } else {
                    movie.setFavorite(true);
                    movie.setType("tv");
                    RoomClient.getInstance(activity.get())
                            .getMovieDatabase()
                            .getMovieDao()
                            .addFavorite(movie);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            activity.get().showMessage(activity.get().getString(R.string.msg_success_add_favorite));
            activity.get().updateWidget();
        }
    }

    private static class RemoveFavoriteTask extends AsyncTask<Void, Void, Void> {
        private int id;
        private WeakReference<MovieDetailActivity> activity;

        RemoveFavoriteTask(MovieDetailActivity activity, int id) {
            this.activity = new WeakReference<>(activity);
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RoomClient.getInstance(activity.get())
                    .getMovieDatabase()
                    .getMovieDao()
                    .removeFavorite(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            activity.get().showMessage(activity.get().getString(R.string.msg_removed_from_favorite));
            activity.get().updateWidget();
        }
    }

    private void updateWidget() {
        new MovieWidget().updateWidgetComponent(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

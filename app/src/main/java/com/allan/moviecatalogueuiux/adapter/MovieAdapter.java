package com.allan.moviecatalogueuiux.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.moviecatalogueuiux.R;
import com.allan.moviecatalogueuiux.data.model.Movie;
import com.allan.moviecatalogueuiux.interfaces.ItemMovieListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ItemMovieListener listener;
    private List<Movie> movies = new ArrayList<>();

    public MovieAdapter(ItemMovieListener listener) {
        this.listener = listener;
    }

    public void setMovieList(List<Movie> list) {
        this.movies = list;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindItem(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView movieImageView;
        private TextView tvMovieTitle, tvMovieScore, tvMoviePopularity;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cvMovie);
            movieImageView = itemView.findViewById(R.id.img_movie);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvMovieScore = itemView.findViewById(R.id.tvVoteAverage);
            tvMoviePopularity = itemView.findViewById(R.id.tvMoviePopularity);

        }

        void bindItem(Movie movie) {
            tvMovieTitle.setText(movie.getTitle() != null ? movie.getTitle() : movie.getOriginalName());
            tvMovieScore.setText(String.valueOf(movie.getVoteAverage()));
            Glide.with(itemView.getContext())
                    .load("http://image.tmdb.org/t/p/w185" + movie.getPosterPath())
                    .apply(new RequestOptions())
                    .transform(new RoundedCorners(30))
                    .into(movieImageView);
            tvMoviePopularity.setText(String.valueOf(movie.getPopularity()));

            cardView.setOnClickListener(getItemClickListener(movie));
        }

        private View.OnClickListener getItemClickListener(final Movie movie) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onMovieClicked(movie);
                }
            };
        }
    }
}

package com.allan.consumerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.consumerapp.R;
import com.allan.consumerapp.model.Movie;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();

    public void setMoviesList(List<Movie> movie) {
        this.movies = movie;
        notifyDataSetChanged();
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
        private ImageView movieImageView;
        private TextView tvMovieTitle;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImageView = itemView.findViewById(R.id.img_movie);
            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
        }

        void bindItem(Movie movie) {
            tvMovieTitle.setText(movie.getMovieTitle() != null ? movie.getMovieTitle() : movie.getMovieOriginalName());
            Glide.with(itemView.getContext())
                    .load("http://image.tmdb.org/t/p/w185" + movie.getMovieBackdrop())
                    .centerCrop()
                    .into(movieImageView);
        }
    }


}

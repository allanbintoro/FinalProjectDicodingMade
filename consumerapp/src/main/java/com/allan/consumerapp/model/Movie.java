package com.allan.consumerapp.model;

public class Movie {
    private String movieTitle;
    private String movieOriginalName;
    private String movieBackdrop;
    private String type;

    public Movie(String movieTitle, String movieOriginalName, String movieBackdrop, String type) {
        this.movieTitle = movieTitle;
        this.movieOriginalName = movieOriginalName;
        this.movieBackdrop = movieBackdrop;
        this.type = type;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieBackdrop() {
        return movieBackdrop;
    }

    public void setMovieBackdrop(String movieBackdrop) {
        this.movieBackdrop = movieBackdrop;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMovieOriginalName() {
        return movieOriginalName;
    }

    public void setMovieOriginalName(String movieOriginalName) {
        this.movieOriginalName = movieOriginalName;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieTitle='" + movieTitle + '\'' +
                ", movieOriginalName='" + movieOriginalName + '\'' +
                ", movieBackdrop='" + movieBackdrop + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

package com.allan.moviecatalogueuiux.data.api;

import com.allan.moviecatalogueuiux.data.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("discover/movie")
    Call<MovieResponse> getMovie(@Query("api_key") String apiKey,
                                 @Query("language") String language);

    @GET("discover/tv")
    Call<MovieResponse> getTvShow(@Query("api_key") String apiKey,
                                  @Query("language") String language);

    @GET("search/movie")
    Call<MovieResponse> getSearchMovies(@Query("api_key") String apiKey,
                                        @Query("language") String language,
                                        @Query("query") String query);

    @GET("search/tv")
    Call<MovieResponse> getSearchTv(@Query("api_key") String apiKey,
                                        @Query("language") String language,
                                        @Query("query") String query);

    @GET("discover/movie")
    Call<MovieResponse> getMovieRelease(@Query("api_key") String apiKey,
                                        @Query("primary_release_date.gte") String gte,
                                        @Query("primary_release_date.lte") String lte);
}

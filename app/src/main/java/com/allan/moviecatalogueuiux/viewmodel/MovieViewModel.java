package com.allan.moviecatalogueuiux.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allan.moviecatalogueuiux.BuildConfig;
import com.allan.moviecatalogueuiux.data.api.RetrofitClient;
import com.allan.moviecatalogueuiux.data.model.Movie;
import com.allan.moviecatalogueuiux.data.model.MovieResponse;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> results = new MutableLiveData<>();
    private MutableLiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private String language = Locale.getDefault().getDisplayLanguage();

    public void getMovies() {
        isLoading.postValue(true);
        new RetrofitClient()
                .getClientMovie()
                .getMovie(BuildConfig.API_KEY,language
                        .equalsIgnoreCase("ENGLISH")
                        ? "en-US" : "id-ID").enqueue(getMovieCallback());
    }

    public void getTvShow() {
        isLoading.postValue(true);
        new RetrofitClient()
                .getClientMovie()
                .getTvShow(BuildConfig.API_KEY,language
                        .equalsIgnoreCase("ENGLISH")
                        ? "en-US" : "id-ID").enqueue(getTvCallback());
    }

    private Callback<MovieResponse> getMovieCallback() {
        return new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call,
                                   Response<MovieResponse> response) {
                isLoading.postValue(false);
                if (response.body() != null) {
                    results.postValue(response.body().getResults());
                } else {
                    message.postValue("Movie not available");
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                isLoading.postValue(false);
                message.postValue("Failed load Movie data");
                Log.e("onFailure: ", t.getMessage());
            }
        };
    }

    private Callback<MovieResponse> getTvCallback() {
        return new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call,
                                   Response<MovieResponse> response) {
                isLoading.postValue(false);
                if (response.body() != null) {
                    results.postValue(response.body().getResults());
                } else {
                    message.postValue("Movie not available");
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                isLoading.postValue(false);
                message.postValue("Failed load Movie data");
                Log.e("onFailure: ", t.getMessage());
            }
        };
    }

    public void fetchSearchMovies(String query) {
        isLoading.setValue(true);
        final RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.getClientMovie().getSearchMovies(BuildConfig.API_KEY, "en-US", query)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call,
                                           @NonNull Response<MovieResponse> response) {
                        if (response.body() != null) {
                            results.postValue(response.body().getResults());
                        } else {
                            message.setValue("Movie Not Available");
                        }
                        isLoading.setValue(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call,
                                          @NonNull Throwable throwable) {
                        message.setValue("Failed load movie data");
                        isLoading.setValue(false);
                    }
                });
    }

    public void fetchSearchTv(String query) {
        isLoading.setValue(true);
        final RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.getClientMovie().getSearchTv(BuildConfig.API_KEY, "en-US", query)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call,
                                           @NonNull Response<MovieResponse> response) {
                        if (response.body() != null) {
                            results.postValue(response.body().getResults());
                        } else {
                            message.setValue("Movie Not Available");
                        }
                        isLoading.setValue(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call,
                                          @NonNull Throwable throwable) {
                        message.setValue("Failed load movie data");
                        isLoading.setValue(false);
                    }
                });
    }

    public LiveData<List<Movie>> getMovieList() {
        return results;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getMessage() {
        return message;
    }
}

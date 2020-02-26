package com.allan.moviecatalogueuiux.service;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.allan.moviecatalogueuiux.BuildConfig;
import com.allan.moviecatalogueuiux.R;
import com.allan.moviecatalogueuiux.data.api.RetrofitClient;
import com.allan.moviecatalogueuiux.data.model.Movie;
import com.allan.moviecatalogueuiux.data.model.MovieResponse;
import com.allan.moviecatalogueuiux.view.activity.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseReminderReceiver extends BroadcastReceiver {
    private static int notificationId = 1;
    private static final int NOTIFICATION_REQUEST_CODE = 100;

    private static final String EXTRA_NOTIF_ID = "extra_notif_id";
    private static final String EXTRA_MOVIE = "extra_movie";
    private static final String CHANNEL_NAME = "daily_release_reminder";
    private static final String CHANNEL_ID = "daily_release_channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        getMovieRelease(context);
    }

    private void showReleaseNotification(Context context, Movie movie, int notificationId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(context.getString(R.string.title_release_reminder))
                .setContentText(movie.getOriginalTitle() + context.getString(R.string.content_release_reminder))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(false)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            builder.setChannelId(CHANNEL_ID);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (notificationManager != null) {
            notificationManager.notify(notificationId, builder.build());
        }
    }

    private void getMovieRelease(final Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final String currentDate = dateFormat.format(new Date());

        final RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.getClientMovie()
                .getMovieRelease(BuildConfig.API_KEY, currentDate, currentDate)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call,
                                           @NonNull Response<MovieResponse> response) {
                        if (response.body() != null) {
                            filterMovieByDate(context, response.body().getResults(), currentDate);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call,
                                          @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void filterMovieByDate(Context context, List<Movie> movies, String date) {
        ArrayList<Movie> filteredMovie = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getReleaseDate().equals(date)) {
                filteredMovie.add(movie);
            }
        }
        setEventNotification(context, filteredMovie);
    }

    public void setEventNotification(final Context context, final ArrayList<Movie> movies) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int id = notificationId;
                for (Movie movie : movies) {
                    showReleaseNotification(context, movie, id);
                    id++;
                }
            }
        }).start();
    }

    private static PendingIntent getReleaseReminderPendingIntent(Context context) {
        Intent intent = new Intent(context, ReleaseReminderReceiver.class);
        return PendingIntent.getBroadcast(context, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void setReleaseReminder(Context context) {
        if (getReleaseReminderPendingIntent(context) != null) {
            cancelReminder(context);
        }

        Intent intent = new Intent(context, ReleaseReminderReceiver.class);
        intent.putExtra(EXTRA_NOTIF_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }

        notificationId += 1;
    }

    public void cancelReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(getReleaseReminderPendingIntent(context));
        }
    }
}

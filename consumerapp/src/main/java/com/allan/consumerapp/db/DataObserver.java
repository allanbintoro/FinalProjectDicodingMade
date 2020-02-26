package com.allan.consumerapp.db;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.allan.consumerapp.interfaces.LoadFavoriteMovieListener;
import com.allan.consumerapp.task.GetFavoriteTask;

import java.lang.ref.WeakReference;

public class DataObserver {

    public static class Observer extends ContentObserver {
        private WeakReference<Context> contextReference;

        public Observer(Handler handler, Context context) {
            super(handler);
            contextReference = new WeakReference<>(context);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new GetFavoriteTask(contextReference.get(), (LoadFavoriteMovieListener) contextReference.get()).execute();
        }
    }
}

package com.allan.moviecatalogueuiux.view.activity.setting;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.allan.moviecatalogueuiux.R;
import com.allan.moviecatalogueuiux.view.fragment.setting.SettingFragment;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.setting, new SettingFragment())
                .commit();
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.settings));
    }
}

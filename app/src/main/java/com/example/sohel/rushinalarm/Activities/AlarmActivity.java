package com.example.sohel.rushinalarm.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sohel.rushinalarm.R;

public class AlarmActivity extends BaseDetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        setupToolbar();
    }
}

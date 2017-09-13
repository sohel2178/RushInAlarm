package com.example.sohel.rushinalarm.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Visibility;
import android.view.View;
import android.widget.ImageView;

import com.example.sohel.rushinalarm.R;

public class AlarmActivity extends BaseDetailActivity implements View.OnClickListener {

    private RecyclerView rvAlarm;
    private ImageView ivAddAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        setupWindowAnimations();
        setupToolbar();

        initView();
    }

    private void initView() {
        rvAlarm = (RecyclerView) findViewById(R.id.rv_alarm);
        ivAddAlarm = (ImageView) findViewById(R.id.add);
        ivAddAlarm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        transitionTo(new Intent(getApplicationContext(),AddAlarmActivity.class));
    }

    private void setupWindowAnimations() {
        Visibility enterTransition = buildEnterTransition();
        getWindow().setEnterTransition(enterTransition);
    }

    private Visibility buildEnterTransition() {
        Fade enterTransition = new Fade();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.excludeTarget(12,true); // You can replace any integer for |R.id.square_red|

        return enterTransition;

    }
}

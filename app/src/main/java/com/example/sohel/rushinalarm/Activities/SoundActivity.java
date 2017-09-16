package com.example.sohel.rushinalarm.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;

import com.example.sohel.rushinalarm.Adapter.SimpleDividerItemDecoration;
import com.example.sohel.rushinalarm.Adapter.SoundAdapter;
import com.example.sohel.rushinalarm.Listener.ItemClickListener;
import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.Model.Sound;
import com.example.sohel.rushinalarm.R;

public class SoundActivity extends BaseDetailActivity implements ItemClickListener {
    private RecyclerView rvSound;

    private SoundAdapter adapter;

    private Sound selectedSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        AlarmData data = (AlarmData) getIntent().getSerializableExtra("data");

        if(data!=null){
            selectedSound = data.getSound();
        }

        setupWindowAnimations();

        setupToolbar();

        adapter = new SoundAdapter(getApplicationContext(),selectedSound);
        adapter.setItemClickListener(this);

        initView();


    }

    private void initView() {
        rvSound = (RecyclerView) findViewById(R.id.rv_sound);
        rvSound.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        rvSound.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvSound.setAdapter(adapter);
    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    @Override
    public void onItemClick(Sound selectedSound) {
        Intent intent = new Intent();
        intent.putExtra("sound",selectedSound);
        setResult(RESULT_OK,intent);

        finish();
    }
}

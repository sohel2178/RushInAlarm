package com.linearbd.sohel.rushinalarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;

import com.linearbd.sohel.rushinalarm.Adapter.SimpleDividerItemDecoration;
import com.linearbd.sohel.rushinalarm.Adapter.SoundAdapter;
import com.linearbd.sohel.rushinalarm.Listener.ItemClickListener;
import com.linearbd.sohel.rushinalarm.Model.AlarmData;
import com.linearbd.sohel.rushinalarm.R;

public class SoundActivity extends BaseDetailActivity implements ItemClickListener {
    private RecyclerView rvSound;

    private SoundAdapter adapter;

    private int selectedSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        AlarmData data = (AlarmData) getIntent().getSerializableExtra("data");

        if(data!=null){
            selectedSoundId = data.getSoundId();
        }

        setupWindowAnimations();

        setupToolbar();

        adapter = new SoundAdapter(getApplicationContext(),selectedSoundId);
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
    public void onItemClick(int selectedSoundId) {
        Intent intent = new Intent();
        intent.putExtra("sound",selectedSoundId);
        setResult(RESULT_OK,intent);

        finish();
    }
}

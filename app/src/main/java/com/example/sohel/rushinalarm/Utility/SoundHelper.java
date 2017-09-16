package com.example.sohel.rushinalarm.Utility;

import android.content.Context;

import com.example.sohel.rushinalarm.Model.Sound;
import com.example.sohel.rushinalarm.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius 03 on 9/16/2017.
 */

public class SoundHelper implements Serializable{
    private Context context;

    private List<Sound> soundList;

    private int[] soundIds = {
            R.raw.alarm,R.raw.awesome_alarm,R.raw.nice_alarm,R.raw.rythmic_alarm,R.raw.sweet_alarm};

    private String[] soundNames;

    public SoundHelper(Context context) {
        soundNames = context.getResources().getStringArray(R.array.alarm_names);
        initSoundList();
    }

    private void initSoundList() {
        soundList = new ArrayList<>();

        for(int i=0;i<soundIds.length;i++){
            Sound sound = new Sound(i,soundNames[i],soundIds[i]);
            soundList.add(sound);
        }
    }


    public List<Sound> getSoundList(){
        return soundList;
    }

    public Sound getSoundById(int id){
        return soundList.get(id);
    }
}

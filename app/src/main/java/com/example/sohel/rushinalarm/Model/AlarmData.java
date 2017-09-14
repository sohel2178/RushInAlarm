package com.example.sohel.rushinalarm.Model;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sohel on 11-09-17.
 */

public class AlarmData implements Serializable {

    private String time;
    private String note;
    private boolean isSet;
    private List<String> repeateDays;
    private int volume;
    private Sound sound;
    private boolean vibration;
    private boolean fadeIn;
    private int snoozeDurationInMin;


    public AlarmData() {
        this("9:30","",false);
    }

    public AlarmData(String time, String note, boolean isSet, List<String> repeateDays, int volume, Sound sound, boolean vibration, boolean fadeIn, int snoozeDurationInMin) {
        this.time = time;
        this.note = note;
        this.isSet = isSet;
        this.repeateDays = repeateDays;
        this.volume = volume;
        this.sound = sound;
        this.vibration = vibration;
        this.fadeIn = fadeIn;
        this.snoozeDurationInMin = snoozeDurationInMin;
    }

    public AlarmData(String time, String note, boolean isSet) {
        this(time,note,isSet,null,50,null,false,false,15);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public List<String> getRepeateDays() {
        return repeateDays;
    }

    public void setRepeateDays(List<String> repeateDays) {
        this.repeateDays = repeateDays;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean isFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(boolean fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getSnoozeDurationInMin() {
        return snoozeDurationInMin;
    }

    public void setSnoozeDurationInMin(int snoozeDurationInMin) {
        this.snoozeDurationInMin = snoozeDurationInMin;
    }
}

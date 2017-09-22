package com.example.sohel.rushinalarm.Model;

import android.net.Uri;

import com.example.sohel.rushinalarm.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sohel on 11-09-17.
 */

public class AlarmData implements Serializable {

    private int id;
    private String time;
    private String note;
    private int isSet;
    private String repeateDays;
    private int volume;
    private int soundId;
    private int vibration;
    private int fadeIn;
    private int snoozeDurationInMin;


    public AlarmData() {
        this("9:30","",0);
    }

    public AlarmData(String time, String note, int isSet, String repeateDays, int volume, int soundId, int vibration, int fadeIn, int snoozeDurationInMin) {
        this(-1,time,note,isSet,repeateDays,volume,soundId,vibration,fadeIn,snoozeDurationInMin);
    }

    public AlarmData(int id, String time, String note, int isSet, String repeateDays, int volume, int soundId, int vibration, int fadeIn, int snoozeDurationInMin) {
        this.id = id;
        this.time = time;
        this.note = note;
        this.isSet = isSet;
        this.repeateDays = repeateDays;
        this.volume = volume;
        this.soundId = soundId;
        this.vibration = vibration;
        this.fadeIn = fadeIn;
        this.snoozeDurationInMin = snoozeDurationInMin;
    }

    public AlarmData(String time, String note, int isSet) {
        this(time,note,isSet,"Never",50,1,0,0,15);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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



    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getIsSet() {
        return isSet;
    }

    public void setIsSet(int isSet) {
        this.isSet = isSet;
    }

    public int getVibration() {
        return vibration;
    }

    public void setVibration(int vibration) {
        this.vibration = vibration;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getSnoozeDurationInMin() {
        return snoozeDurationInMin;
    }

    public void setSnoozeDurationInMin(int snoozeDurationInMin) {
        this.snoozeDurationInMin = snoozeDurationInMin;
    }

    public String getRepeateDays() {
        return repeateDays;
    }

    public void setRepeateDays(String repeateDays) {
        this.repeateDays = repeateDays;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    public int getHour(){
        String[] hhh = getTime().split(":");

        return Integer.parseInt(hhh[0]);
    }

    public int getMinutes(){
        String[] hhh = getTime().split(":");
        return Integer.parseInt(hhh[1]);
    }
}

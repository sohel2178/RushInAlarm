package com.example.sohel.rushinalarm.Model;

import java.io.Serializable;

/**
 * Created by sohel on 11-09-17.
 */

public class AlarmData implements Serializable {

    private String time;
    private String desc;
    private boolean isSet;


    public AlarmData() {
    }

    public AlarmData(String time, String desc, boolean isSet) {
        this.time = time;
        this.desc = desc;
        this.isSet = isSet;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }
}

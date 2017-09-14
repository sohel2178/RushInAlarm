package com.example.sohel.rushinalarm.Model;

import java.io.Serializable;

/**
 * Created by Genius 03 on 9/14/2017.
 */

public class Sound implements Serializable {

    private String name;
    private int sound_id;

    public Sound() {
    }

    public Sound(String name, int sound_id) {
        this.name = name;
        this.sound_id = sound_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSound_id() {
        return sound_id;
    }

    public void setSound_id(int sound_id) {
        this.sound_id = sound_id;
    }
}

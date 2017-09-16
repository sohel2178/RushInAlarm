package com.example.sohel.rushinalarm.Model;

import java.io.Serializable;

/**
 * Created by Genius 03 on 9/14/2017.
 */

public class Sound implements Serializable {

    private int id;
    private String name;
    private int res_id;

    public Sound() {
    }

    public Sound(int id, String name, int res_id) {
        this.id = id;
        this.name = name;
        this.res_id = res_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }
}

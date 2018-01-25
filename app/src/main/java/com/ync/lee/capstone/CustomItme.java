package com.ync.lee.capstone;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lee on 2017. 11. 28..
 */

public class CustomItme implements Serializable{

    String menu = "";
    int good =0;
    int bad=0;
    CustomItme(){
        JSONObject
    }
    CustomItme(String menu){
        this.menu=menu;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getBad() {
        return bad;
    }

    public void setBad(int bad) {
        this.bad = bad;
    }
}

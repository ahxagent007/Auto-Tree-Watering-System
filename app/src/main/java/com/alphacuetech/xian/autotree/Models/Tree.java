package com.alphacuetech.xian.autotree.Models;

public class Tree {

    int tree_id;
    String tree_name;
    int water_min;
    int water_max;
    int temp_min;
    int temp_max;
    String img;

    public Tree() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getTree_id() {
        return tree_id;
    }

    public void setTree_id(int tree_id) {
        this.tree_id = tree_id;
    }

    public String getTree_name() {
        return tree_name;
    }

    public void setTree_name(String tree_name) {
        this.tree_name = tree_name;
    }

    public int getWater_min() {
        return water_min;
    }

    public void setWater_min(int water_min) {
        this.water_min = water_min;
    }

    public int getWater_max() {
        return water_max;
    }

    public void setWater_max(int water_max) {
        this.water_max = water_max;
    }

    public int getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(int temp_min) {
        this.temp_min = temp_min;
    }

    public int getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(int temp_max) {
        this.temp_max = temp_max;
    }
}

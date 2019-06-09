package com.example.lianghw.easyeat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoreData implements Serializable {
    public List<Food> all_food_list = new ArrayList<>();
    public List<Food> order_food_list = new ArrayList<>();
    public List<String> food_type_list = new ArrayList<>();

    private static StoreData instance;

    public StoreData(){

    }

    public static StoreData getInstance() {
        if(instance == null){
            instance = new StoreData();
        }
        return instance;
    }
}

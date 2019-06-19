package com.example.lianghw.easyeat;

import java.util.HashMap;

public class TypeListViewItem {
    public int type;
    public HashMap<String, Object> map;

    public TypeListViewItem(int type, HashMap<String, Object> map) {
        this.type = type;
        this.map = map;
    }
}

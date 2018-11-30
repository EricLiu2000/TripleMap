package edu.umich.triplemap;


import java.util.ArrayList;

public class EventList extends ArrayList {
    public void remove(String s) {
        for(int i = 0; i < size(); i++) {
            if(get(i).toString().equals(s)) {
                remove(i);
                break;
            }
        }
    }

    public Object get(String s) {
        for(int i = 0; i < size(); i++) {
            if(get(i).toString().equals(s)) {
                return get(i);
            }
        }
        return null;
    }
}

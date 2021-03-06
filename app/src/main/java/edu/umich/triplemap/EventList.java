package edu.umich.triplemap;


import java.util.ArrayList;

public class EventList<E> extends ArrayList {

    public ArrayList<String> getEventNames() {
        ArrayList<String> names = new ArrayList<>();
        for(int i = 0; i < size(); i++) {
            names.add(get(i).toString());
        }
        return names;
    }

    public void remove(String s) {
        for(int i = 0; i < size(); i++) {
            if(get(i).toString().equals(s)) {
                remove(i);
                break;
            }
        }
    }

    @Override
    public Event get(int index) {
        return (Event) super.get(index);
    }

    public E get(String s) {
        for(int i = 0; i < size(); i++) {
            if(get(i).toString().equals(s)) {
                return (E) get(i);
            }
        }
        return null;
    }
}

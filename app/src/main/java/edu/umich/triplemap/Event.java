package edu.umich.triplemap;

public class Event {
    public boolean getIsWeekly() {
        return isWeekly;
    }

    public void setIsWeekly(boolean weekly) {
        isWeekly = weekly;
    }

    private boolean isWeekly = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null) {
            return;
        }

        this.name = name;
    }

    private String name = "";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if(address == null) {
            return;
        }
        this.address = address;
    }

    private String address = "";

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        if(room == null) {
            return;
        }
        this.room = room;
    }

    private String room = "";

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        if(date == null) {
            return;
        }
        this.date = date;
    }

    private String date = "";

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        if(startTime == null) {
            return;
        }
        this.startTime = startTime;
    }

    private String startTime = "";

    @Override
    public String toString() {
        return name;
    }
}

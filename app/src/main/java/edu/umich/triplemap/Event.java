package edu.umich.triplemap;

import org.joda.time.DateTime;

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

    public long getLengthInSeconds() {
        return lengthInSeconds;
    }

    public void setLengthInSeconds(long lengthInSeconds) {
        this.lengthInSeconds = lengthInSeconds;
    }

    private long lengthInSeconds = 0;

    /**
     *
     * @return [Hour, Minute]
     */
    public int[] getProcessedTime() {
        String timeCopy = startTime;
        boolean am = false;
        boolean pm = false;

        if(timeCopy.contains("AM") || timeCopy.contains("am")) {
            am = true;
            timeCopy = timeCopy.replaceAll("AM", "");
            timeCopy = timeCopy.replaceAll("am", "");
        }

        if(timeCopy.contains("PM") || timeCopy.contains("pm")) {
            pm = true;
            timeCopy = timeCopy.replaceAll("PM", "");
            timeCopy = timeCopy.replaceAll("pm", "");
        }

        String[] time = timeCopy.split(":");
        if(time.length == 2) {
            if(am) {
                return new int[] {Integer.valueOf(time[0]), Integer.valueOf(time[1])};
            } else if(pm) {
                return new int[] {Integer.valueOf(time[0]) + 12, Integer.valueOf(time[1])};
            } else {
                return new int[] {Integer.valueOf(time[0]), Integer.valueOf(time[1])};
            }

        } else {
            //Assume only hour specified
            if(am) {
                return new int[] {Integer.valueOf(time[0]), 0};
            } else if(pm) {
                return new int[] {Integer.valueOf(time[0]) + 12, 0};
            } else {
                return new int[] {Integer.valueOf(time[0]), 0};
            }
        }
    }

    /**
     *
     * @return [Month, Day, Year]
     */
    public int[] getProcessedDate() {
        String dateCopy = this.date;
        String[] date = dateCopy.split("/");
        //month/day/year(19 vs 2019)
        if(date.length == 3) {
            //Only 2 digits specified
            int year = 2000;
            if(Integer.valueOf(date[2]) < 100) {
                year = year + Integer.valueOf(date[2]);
            } else {
                year = Integer.valueOf(date[2]);
            }
            return new int[] {Integer.valueOf(date[0]), Integer.valueOf(date[1]), year};
        } else {
            //Assume month/day specified, year is 2018
            return new int[] {Integer.valueOf(date[0]), Integer.valueOf(date[1]), 2018};
        }
    }

    public DateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(DateTime departureTime) {
        this.departureTime = departureTime;
    }

    private DateTime departureTime;

    @Override
    public String toString() {
        return name;
    }
}

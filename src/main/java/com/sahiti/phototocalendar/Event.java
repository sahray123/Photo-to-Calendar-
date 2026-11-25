package com.sahiti.phototocalendar;
import java.time.ZoneId;


public class Event {
    private String event;
    private String date; 
    private String time; 
    private String location;
    private String timeZone;
    private String abbZone; 

    public Event(String event, String date, String time, String location) {
        this.event = event;
        this.date = date;
        this.time = time;
        this.location = location;
        this.timeZone = ZoneId.systemDefault().toString();
        if (timeZone.equals("America/New_York") || timeZone.equals("America/Indiana/Indianapolis")) {
            abbZone = "EST";
        } else if (timeZone.equals("America/Chicago")) {
            abbZone = "CST";
        } else if (timeZone.equals("America/Denver")) {
            abbZone = "MST";
        } else if (timeZone.equals("America/Los_Angeles")) {
            abbZone = "PST";
        } else {
            this.abbZone = this.timeZone;
        }
    }

    public String getEvent() {
        return this.event;
    }
    public void setEvent(String event) {
        this.event = event;
    }


    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getZone() {
        return this.timeZone;
    }
    public void setZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getAbb() {
        return this.abbZone;
    }
    public void setAbb(String abbZone) {
        String clean = abbZone.trim().toUpperCase();
        if (clean.equals("EST") || clean.equals("EDT")) {
            this.abbZone = "EST";
            this.timeZone = "America/New_York";
        } else if (clean.equals("CST") || clean.equals("CDT")) {
            this.abbZone = "CST";
            this.timeZone = "America/Chicago";
        } else if (clean.equals("MST") || clean.equals("MDT")) {
            this.abbZone = "MST";
            this.timeZone = "America/Denver";
        } else if (clean.equals("PST") || clean.equals("PDT")) {
            this.abbZone = "PST"; 
            this.timeZone = "America/Los_Angeles";
        } else {
            System.out.println("Invalid time zone. Please enter EST, CST, MST or PST.");
        }
    }

    @Override 
    public String toString() {
        return "\n" + "1. Event: " + event + "\n" + "2. Location: " + location + "\n" + "3. Date: " + date + "\n" + "4. Time: " + time + "\n" + "5. Time Zone: " + abbZone + "\n";

    }


}
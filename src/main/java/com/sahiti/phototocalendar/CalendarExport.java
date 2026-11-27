package com.sahiti.phototocalendar;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;

public class CalendarExport {
    private ArrayList<Event> events;  

    public CalendarExport(ArrayList<Event> events) {
        this.events = events;
    }

    private ZonedDateTime combinedEvent(ZoneId zone, LocalDate date, LocalTime time) {
        ZonedDateTime combined = ZonedDateTime.of(date, time, zone);
        return combined;
    }

    public ZonedDateTime startEvent(Event info) {
        ZoneId zone = ZoneId.of(info.getZone());
        return combinedEvent(zone, info.getParsedDate(), info.getStartTime());
      
    }

    public ZonedDateTime endEvent(Event info) {
        ZoneId zone = ZoneId.of(info.getZone());
        ZonedDateTime ans = combinedEvent(zone, info.getParsedDate(), info.getEndTime());
        if (!ans.isAfter(startEvent(info))) {
            ans = ans.plusDays(1);
        }
        return ans;
      
    }

     
    public VEvent buildEvent(Event info) {
        VEvent ev = new VEvent();
        //create VEvent

        ev.add(new Summary(info.getEvent()));
        ev.add(new Location(info.getLocation()));
        //add event title and location
        
        ev.add(new DtStart<>(startEvent(info)));
        ev.add(new DtEnd<>(endEvent(info)));
        //add start and end time

        ev.add(new RandomUidGenerator().generateUid());
        //add UID

        return ev;

    }

    public void exportCalendar(String file) throws IOException {
        Calendar calendar = new Calendar(); 
        calendar.add(new ProdId("-//PhotoToCalendar//EN"));
        calendar.add(ImmutableVersion.VERSION_2_0);

        for (Event info : events) {
            calendar.add(buildEvent(info));
        }
        

        try (FileOutputStream fout = new FileOutputStream(file)) {
            CalendarOutputter calOut = new CalendarOutputter();
            calOut.output(calendar, fout);
        } 

    }
        

    

   
    



   
    
}

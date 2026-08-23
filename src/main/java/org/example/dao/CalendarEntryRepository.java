package org.example.dao;

import org.example.entity.CalendarEntry;

public class CalendarEntryRepository extends GenericDao<CalendarEntry> {

    public CalendarEntryRepository() {
        super(CalendarEntry.class);
    }
}

package org.example.service;

import org.example.dao.CalendarEntryRepository;
import org.example.entity.CalendarEntry;
import org.example.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarEntryService {

    private final CalendarEntryRepository calendarEntryRepository = new CalendarEntryRepository();

    // legt für den Tag einen neuen Eintrag an oder überschreibt den vorhandenen
    public CalendarEntry saveEntry(LocalDate date, boolean trained, String muscleGroup,
                                    int calories, double fat, double carbohydrates, User user) {
        CalendarEntry entry = findEntry(user, date);

        if (entry == null) {
            entry = new CalendarEntry();
            entry.setDate(date);
            entry.setUser(user);
        }

        entry.setTrained(trained);
        entry.setMuscleGroup(muscleGroup);
        entry.setCalories(calories);
        entry.setFat(fat);
        entry.setCarbohydrates(carbohydrates);

        if (entry.getId() == 0) {
            calendarEntryRepository.save(entry);
        } else {
            calendarEntryRepository.update(entry);
        }

        return entry;
    }

    public CalendarEntry findEntry(User user, LocalDate date) {
        for (CalendarEntry entry : calendarEntryRepository.findAll()) {
            if (entry.getUser().getId() == user.getId() && entry.getDate().equals(date)) {
                return entry;
            }
        }

        return null;
    }

    public List<CalendarEntry> findEntriesForUser(User user) {
        List<CalendarEntry> entries = new ArrayList<>();

        for (CalendarEntry entry : calendarEntryRepository.findAll()) {
            if (entry.getUser().getId() == user.getId()) {
                entries.add(entry);
            }
        }

        return entries;
    }

    public void deleteAllEntriesForUser(User user) {
        for (CalendarEntry entry : findEntriesForUser(user)) {
            calendarEntryRepository.delete(entry);
        }
    }
}

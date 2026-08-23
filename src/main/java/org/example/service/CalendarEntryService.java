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
    public CalendarEntry speichereEintrag(LocalDate datum, boolean trainiert, String muskelgruppe,
                                           int kalorien, double fett, double kohlenhydrate, User user) {
        CalendarEntry eintrag = findeEintrag(user, datum);

        if (eintrag == null) {
            eintrag = new CalendarEntry();
            eintrag.setDatum(datum);
            eintrag.setUser(user);
        }

        eintrag.setTrainiert(trainiert);
        eintrag.setMuskelgruppe(muskelgruppe);
        eintrag.setKalorien(kalorien);
        eintrag.setFett(fett);
        eintrag.setKohlenhydrate(kohlenhydrate);

        if (eintrag.getId() == 0) {
            calendarEntryRepository.save(eintrag);
        } else {
            calendarEntryRepository.update(eintrag);
        }

        return eintrag;
    }

    public CalendarEntry findeEintrag(User user, LocalDate datum) {
        for (CalendarEntry eintrag : calendarEntryRepository.findAll()) {
            if (eintrag.getUser().getId() == user.getId() && eintrag.getDatum().equals(datum)) {
                return eintrag;
            }
        }

        return null;
    }

    public List<CalendarEntry> findeEintraegeFuerUser(User user) {
        List<CalendarEntry> eintraege = new ArrayList<>();

        for (CalendarEntry eintrag : calendarEntryRepository.findAll()) {
            if (eintrag.getUser().getId() == user.getId()) {
                eintraege.add(eintrag);
            }
        }

        return eintraege;
    }

    public void loescheAlleEintraegeFuerUser(User user) {
        for (CalendarEntry eintrag : findeEintraegeFuerUser(user)) {
            calendarEntryRepository.delete(eintrag);
        }
    }
}

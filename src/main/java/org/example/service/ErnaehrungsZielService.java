package org.example.service;

import org.example.dao.ErnaehrungsZielRepository;
import org.example.entity.ErnaehrungsZiel;
import org.example.entity.User;

public class ErnaehrungsZielService {

    private final ErnaehrungsZielRepository ernaehrungsZielRepository = new ErnaehrungsZielRepository();

    // speichert das zuletzt im BMI-Rechner berechnete Ziel für den User, überschreibt ein vorhandenes
    public ErnaehrungsZiel speichereZiel(int kalorien, double protein, double fett, double kohlenhydrate,
                                          String ziel, User user) {
        ErnaehrungsZiel ernaehrungsZiel = findZielFuerUser(user);

        if (ernaehrungsZiel == null) {
            ernaehrungsZiel = new ErnaehrungsZiel();
            ernaehrungsZiel.setUser(user);
        }

        ernaehrungsZiel.setKalorien(kalorien);
        ernaehrungsZiel.setProtein(protein);
        ernaehrungsZiel.setFett(fett);
        ernaehrungsZiel.setKohlenhydrate(kohlenhydrate);
        ernaehrungsZiel.setZiel(ziel);

        if (ernaehrungsZiel.getId() == 0) {
            ernaehrungsZielRepository.save(ernaehrungsZiel);
        } else {
            ernaehrungsZielRepository.update(ernaehrungsZiel);
        }

        return ernaehrungsZiel;
    }

    public ErnaehrungsZiel findZielFuerUser(User user) {
        for (ErnaehrungsZiel ernaehrungsZiel : ernaehrungsZielRepository.findAll()) {
            if (ernaehrungsZiel.getUser().getId() == user.getId()) {
                return ernaehrungsZiel;
            }
        }

        return null;
    }

}

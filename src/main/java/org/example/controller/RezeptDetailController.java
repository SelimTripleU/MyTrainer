package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.entity.Rezept;

import java.util.Locale;

public class RezeptDetailController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label naehrwerteLabel;

    @FXML
    private Label rezeptTextLabel;

    public void init(Rezept rezept) {
        nameLabel.setText(rezept.getName());
        naehrwerteLabel.setText(String.format(Locale.GERMANY,
                "%d kcal – %.0fg Protein, %.0fg Fett, %.0fg Kohlenhydrate",
                rezept.getKalorien(), rezept.getProtein(), rezept.getFett(), rezept.getKohlenhydrate()));
        rezeptTextLabel.setText(rezept.getRezeptText());
    }
}

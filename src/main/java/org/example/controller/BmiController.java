package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.Locale;

public class BmiController {

    @FXML
    private ChoiceBox<String> geschlechtChoiceBox;

    @FXML
    private TextField alterField;

    @FXML
    private TextField groesseField;

    @FXML
    private TextField gewichtField;

    @FXML
    private ChoiceBox<String> aktivitaetChoiceBox;

    @FXML
    private ChoiceBox<String> zielChoiceBox;

    @FXML
    private Label bmiLabel;

    @FXML
    private Label kategorieLabel;

    @FXML
    private Label kalorienLabel;

    @FXML
    private Label proteinLabel;

    @FXML
    private Label fettLabel;

    @FXML
    private Label kohlenhydrateLabel;

    // Aktivitätsfaktoren nach der PAL-Skala, mit denen der Grundumsatz auf den Gesamtkalorienbedarf hochgerechnet wird
    private static final double[] AKTIVITAETS_FAKTOREN = {1.2, 1.375, 1.55, 1.725, 1.9};

    // übliches Kaloriendefizit bzw. -überschuss für eine moderate, gesunde Gewichtsveränderung
    private static final double[] ZIEL_KALORIEN_ANPASSUNG = {-500, 0, 500};

    @FXML
    private void initialize() {
        geschlechtChoiceBox.getItems().addAll("Männlich", "Weiblich");
        geschlechtChoiceBox.setValue("Männlich");

        aktivitaetChoiceBox.getItems().addAll(
                "Sitzend (kaum Bewegung)",
                "Leicht aktiv (1-3x Sport/Woche)",
                "Mäßig aktiv (3-5x Sport/Woche)",
                "Aktiv (6-7x Sport/Woche)",
                "Sehr aktiv (körperliche Arbeit/Leistungssport)");
        aktivitaetChoiceBox.setValue("Leicht aktiv (1-3x Sport/Woche)");

        zielChoiceBox.getItems().addAll("Abnehmen", "Gewicht halten", "Zunehmen");
        zielChoiceBox.setValue("Gewicht halten");

        // Alter und Größe: nur ganze Zahlen, damit man sich nicht vertippen kann
        alterField.setTextFormatter(new TextFormatter<>(this::nurGanzeZahlen));
        groesseField.setTextFormatter(new TextFormatter<>(this::nurGanzeZahlen));

        // Gewicht: nur Ziffern und ein Komma, insgesamt maximal 4 Ziffern (z.B. 88,24)
        gewichtField.setTextFormatter(new TextFormatter<>(this::nurGewicht));
    }

    private TextFormatter.Change nurGanzeZahlen(TextFormatter.Change change) {
        String neuerText = change.getControlNewText();

        if (!neuerText.matches("\\d{0,3}")) {
            return null;
        }

        return change;
    }

    private TextFormatter.Change nurGewicht(TextFormatter.Change change) {
        String neuerText = change.getControlNewText();

        if (!neuerText.matches("\\d{0,4}(,\\d{0,4})?")) {
            return null;
        }

        long ziffernAnzahl = neuerText.chars().filter(Character::isDigit).count();
        if (ziffernAnzahl > 4) {
            return null;
        }

        return change;
    }

    @FXML
    private void onBerechnen() {
        if (alterField.getText().isBlank() || groesseField.getText().isBlank() || gewichtField.getText().isBlank()) {
            zeigeFehler("Bitte Alter, Größe und Gewicht ausfüllen.");
            return;
        }

        int alter = Integer.parseInt(alterField.getText());
        int groesseCm = Integer.parseInt(groesseField.getText());
        double gewichtKg = Double.parseDouble(gewichtField.getText().replace(',', '.'));

        if (alter <= 0 || groesseCm <= 0 || gewichtKg <= 0) {
            zeigeFehler("Bitte gültige Werte größer als 0 eingeben.");
            return;
        }

        double groesseM = groesseCm / 100.0;
        double bmi = gewichtKg / (groesseM * groesseM);

        bmiLabel.setText(String.format(Locale.GERMANY, "Dein BMI: %.1f", bmi));
        kategorieLabel.setText("Kategorie: " + bmiKategorie(bmi));

        boolean istMaennlich = geschlechtChoiceBox.getValue().equals("Männlich");
        double grundumsatz = istMaennlich
                ? 10 * gewichtKg + 6.25 * groesseCm - 5 * alter + 5
                : 10 * gewichtKg + 6.25 * groesseCm - 5 * alter - 161;

        double aktivitaetsFaktor = AKTIVITAETS_FAKTOREN[aktivitaetChoiceBox.getSelectionModel().getSelectedIndex()];
        double erhaltungsKalorien = grundumsatz * aktivitaetsFaktor;

        double zielAnpassung = ZIEL_KALORIEN_ANPASSUNG[zielChoiceBox.getSelectionModel().getSelectedIndex()];
        double kalorienBedarf = erhaltungsKalorien + zielAnpassung;

        // Nährstoffverteilung: 1,8g Protein pro kg Körpergewicht, 25% der Kalorien als Fett, Rest Kohlenhydrate
        double proteinG = gewichtKg * 1.8;
        double proteinKcal = proteinG * 4;

        double fettKcal = kalorienBedarf * 0.25;
        double fettG = fettKcal / 9;

        double kohlenhydrateKcal = kalorienBedarf - proteinKcal - fettKcal;
        double kohlenhydrateG = kohlenhydrateKcal / 4;

        kalorienLabel.setText(String.format(Locale.GERMANY, "Kalorienziel: %.0f kcal/Tag", kalorienBedarf));
        proteinLabel.setText(String.format(Locale.GERMANY, "Protein: %.0f g/Tag", proteinG));
        fettLabel.setText(String.format(Locale.GERMANY, "Fett: %.0f g/Tag", fettG));
        kohlenhydrateLabel.setText(String.format(Locale.GERMANY, "Kohlenhydrate: %.0f g/Tag", kohlenhydrateG));
    }

    private String bmiKategorie(double bmi) {
        if (bmi < 18.5) {
            return "Untergewicht";
        } else if (bmi < 25) {
            return "Normalgewicht";
        } else if (bmi < 30) {
            return "Übergewicht";
        } else {
            return "Adipositas";
        }
    }

    private void zeigeFehler(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }
}

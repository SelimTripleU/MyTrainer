package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.example.entity.User;
import org.example.service.BodyMeasurementService;
import org.example.service.UserService;

import java.time.LocalDate;
import java.util.prefs.Preferences;

public class PrimaryController {

    @FXML
    private TextField nameField;

    @FXML
    private DatePicker datumPicker;

    @FXML
    private TextField altesGewichtField;

    @FXML
    private TextField neuesGewichtField;

    private final UserService userService = new UserService();
    private final BodyMeasurementService bodyMeasurementService = new BodyMeasurementService();

    // speichert Name und Altes Gewicht lokal, damit sie beim nächsten Start noch da sind
    private final Preferences prefs = Preferences.userNodeForPackage(PrimaryController.class);

    @FXML
    private void initialize() {
        nameField.setText(prefs.get("name", ""));
        altesGewichtField.setText(prefs.get("altesGewicht", ""));

        nameField.textProperty().addListener((obs, oldValue, newValue) -> prefs.put("name", newValue));
        altesGewichtField.textProperty().addListener((obs, oldValue, newValue) -> prefs.put("altesGewicht", newValue));

        // Gewicht: nur Ziffern und ein Komma, insgesamt maximal 4 Ziffern (z.B. 88,24)
        neuesGewichtField.setTextFormatter(new TextFormatter<>(this::filtereGewicht));
    }

    private TextFormatter.Change filtereGewicht(TextFormatter.Change change) {
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
    private void onReset() {
        nameField.clear();
        datumPicker.setValue(null);
        altesGewichtField.clear();
        neuesGewichtField.clear();

        prefs.remove("name");
        prefs.remove("altesGewicht");
    }

    @FXML
    private void onSpeichern() {
        String name = nameField.getText();
        LocalDate datum = datumPicker.getValue();

        if (name == null || name.isBlank() || datum == null || neuesGewichtField.getText().isBlank()) {
            zeigeFehler("Bitte Name, Datum und Neues Gewicht ausfüllen.");
            return;
        }

        double neuesGewicht;
        try {
            neuesGewicht = Double.parseDouble(neuesGewichtField.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            zeigeFehler("Neues Gewicht muss eine Zahl sein.");
            return;
        }

        User user = userService.findUserByName(name);
        if (user == null) {
            user = userService.createUser(name, null, 0, null);
        }

        // beim allerersten Eintrag wird das neue Gewicht auch als Altes Gewicht übernommen
        if (altesGewichtField.getText().isBlank()) {
            altesGewichtField.setText(neuesGewichtField.getText());
        }

        bodyMeasurementService.createBodyMeasurement(datum, neuesGewicht, 0, 0, user);
    }

    private void zeigeFehler(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }
}
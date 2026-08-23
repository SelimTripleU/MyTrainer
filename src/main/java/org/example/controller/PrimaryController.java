package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;
import org.example.entity.User;
import org.example.service.BodyMeasurementService;
import org.example.service.CalendarEntryService;
import org.example.service.UserService;

import java.io.IOException;
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
    private final CalendarEntryService calendarEntryService = new CalendarEntryService();

    // Referenz auf einen bereits geöffneten Kalender, damit er sich bei einem Reset sofort aktualisiert
    private CalendarController offenerCalendarController;

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
    private void onBmiRechner() {
        User user = ermittleAktuellenUser();
        if (user == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("bmi.fxml"));
            Parent root = fxmlLoader.load();

            BmiController bmiController = fxmlLoader.getController();
            bmiController.init(user);

            Stage bmiStage = new Stage();
            bmiStage.setTitle("BMI-Rechner");
            bmiStage.initModality(Modality.APPLICATION_MODAL);
            bmiStage.setScene(new Scene(root));
            bmiStage.show();
        } catch (IOException e) {
            zeigeFehler("BMI-Rechner konnte nicht geöffnet werden.");
        }
    }

    @FXML
    private void onKalender() {
        User user = ermittleAktuellenUser();
        if (user == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("calendar.fxml"));
            Parent root = fxmlLoader.load();

            offenerCalendarController = fxmlLoader.getController();
            offenerCalendarController.init(user);

            Stage calendarStage = new Stage();
            calendarStage.setTitle("Trainingskalender");
            calendarStage.setScene(new Scene(root));
            calendarStage.show();
        } catch (IOException e) {
            zeigeFehler("Kalender konnte nicht geöffnet werden.");
        }
    }

    @FXML
    private void onUebungen() {
        User user = ermittleAktuellenUser();
        if (user == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("uebungen.fxml"));
            Parent root = fxmlLoader.load();

            UebungenController uebungenController = fxmlLoader.getController();
            uebungenController.init(user);

            Stage uebungenStage = new Stage();
            uebungenStage.setTitle("Übungen");
            uebungenStage.setScene(new Scene(root));
            uebungenStage.show();
        } catch (IOException e) {
            zeigeFehler("Übungen konnten nicht geöffnet werden.");
        }
    }

    @FXML
    private void onEssen() {
        User user = ermittleAktuellenUser();
        if (user == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("essen.fxml"));
            Parent root = fxmlLoader.load();

            EssenController essenController = fxmlLoader.getController();
            essenController.init(user);

            Stage essenStage = new Stage();
            essenStage.setTitle("Essen");
            essenStage.initModality(Modality.APPLICATION_MODAL);
            essenStage.setScene(new Scene(root));
            essenStage.show();
        } catch (IOException e) {
            zeigeFehler("Essen konnte nicht geöffnet werden.");
        }
    }

    @FXML
    private void onDiagramm() {
        User user = ermittleAktuellenUser();
        if (user == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("diagramm.fxml"));
            Parent root = fxmlLoader.load();

            DiagrammController diagrammController = fxmlLoader.getController();
            diagrammController.init(user);

            Stage diagrammStage = new Stage();
            diagrammStage.setTitle("Gewichtsverlauf");
            diagrammStage.setScene(new Scene(root));
            diagrammStage.show();
        } catch (IOException e) {
            zeigeFehler("Diagramm konnte nicht geöffnet werden.");
        }
    }

    // liefert den User zum eingegebenen Namen, legt ihn bei Bedarf an; zeigt einen Fehler und liefert null, wenn kein Name eingegeben wurde
    private User ermittleAktuellenUser() {
        String name = nameField.getText();
        if (name == null || name.isBlank()) {
            zeigeFehler("Bitte zuerst einen Namen eingeben.");
            return null;
        }

        User user = userService.findUserByName(name);
        if (user == null) {
            user = userService.createUser(name, null, 0, null);
        }
        return user;
    }

    @FXML
    private void onReset() {
        User user = userService.findUserByName(nameField.getText());
        if (user != null) {
            calendarEntryService.loescheAlleEintraegeFuerUser(user);
        }

        nameField.clear();
        datumPicker.setValue(null);
        altesGewichtField.clear();
        neuesGewichtField.clear();

        prefs.remove("name");
        prefs.remove("altesGewicht");

        if (offenerCalendarController != null) {
            offenerCalendarController.aktualisiere();
        }
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
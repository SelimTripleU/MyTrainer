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
    private DatePicker datePicker;

    @FXML
    private TextField oldWeightField;

    @FXML
    private TextField newWeightField;

    private final UserService userService = new UserService();
    private final BodyMeasurementService bodyMeasurementService = new BodyMeasurementService();
    private final CalendarEntryService calendarEntryService = new CalendarEntryService();

    // Referenz auf einen bereits geöffneten Kalender, damit er sich bei einem Reset sofort aktualisiert
    private CalendarController openCalendarController;

    // speichert Name und Altes Gewicht lokal, damit sie beim nächsten Start noch da sind
    private final Preferences prefs = Preferences.userNodeForPackage(PrimaryController.class);

    @FXML
    private void initialize() {
        nameField.setText(prefs.get("name", ""));
        oldWeightField.setText(prefs.get("oldWeight", ""));

        nameField.textProperty().addListener((obs, oldValue, newValue) -> prefs.put("name", newValue));
        oldWeightField.textProperty().addListener((obs, oldValue, newValue) -> prefs.put("oldWeight", newValue));

        // Gewicht: nur Ziffern und ein Komma, insgesamt maximal 4 Ziffern (z.B. 88,24)
        newWeightField.setTextFormatter(new TextFormatter<>(this::filterWeight));
    }

    private TextFormatter.Change filterWeight(TextFormatter.Change change) {
        String newText = change.getControlNewText();

        if (!newText.matches("\\d{0,4}(,\\d{0,4})?")) {
            return null;
        }

        long digitCount = newText.chars().filter(Character::isDigit).count();
        if (digitCount > 4) {
            return null;
        }

        return change;
    }

    @FXML
    private void onBmiCalculator() {
        User user = getCurrentUser();
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
            showError("BMI-Rechner konnte nicht geöffnet werden.");
        }
    }

    @FXML
    private void onCalendar() {
        User user = getCurrentUser();
        if (user == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("calendar.fxml"));
            Parent root = fxmlLoader.load();

            openCalendarController = fxmlLoader.getController();
            openCalendarController.init(user);

            Stage calendarStage = new Stage();
            calendarStage.setTitle("Trainingskalender");
            calendarStage.setScene(new Scene(root));
            calendarStage.show();
        } catch (IOException e) {
            showError("Kalender konnte nicht geöffnet werden.");
        }
    }

    @FXML
    private void onExercises() {
        User user = getCurrentUser();
        if (user == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("uebungen.fxml"));
            Parent root = fxmlLoader.load();

            ExercisesController exercisesController = fxmlLoader.getController();
            exercisesController.init(user);

            Stage exercisesStage = new Stage();
            exercisesStage.setTitle("Übungen");
            exercisesStage.setScene(new Scene(root));
            exercisesStage.show();
        } catch (IOException e) {
            showError("Übungen konnten nicht geöffnet werden.");
        }
    }

    @FXML
    private void onFood() {
        User user = getCurrentUser();
        if (user == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("essen.fxml"));
            Parent root = fxmlLoader.load();

            FoodController foodController = fxmlLoader.getController();
            foodController.init(user);

            Stage foodStage = new Stage();
            foodStage.setTitle("Essen");
            foodStage.initModality(Modality.APPLICATION_MODAL);
            foodStage.setScene(new Scene(root));
            foodStage.show();
        } catch (IOException e) {
            showError("Essen konnte nicht geöffnet werden.");
        }
    }

    @FXML
    private void onDiagram() {
        User user = getCurrentUser();
        if (user == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("diagramm.fxml"));
            Parent root = fxmlLoader.load();

            DiagramController diagramController = fxmlLoader.getController();
            diagramController.init(user);

            Stage diagramStage = new Stage();
            diagramStage.setTitle("Gewichtsverlauf");
            diagramStage.setScene(new Scene(root));
            diagramStage.show();
        } catch (IOException e) {
            showError("Diagramm konnte nicht geöffnet werden.");
        }
    }

    // liefert den User zum eingegebenen Namen, legt ihn bei Bedarf an; zeigt einen Fehler und liefert null, wenn kein Name eingegeben wurde
    private User getCurrentUser() {
        String name = nameField.getText();
        if (name == null || name.isBlank()) {
            showError("Bitte zuerst einen Namen eingeben.");
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
            calendarEntryService.deleteAllEntriesForUser(user);
        }

        nameField.clear();
        datePicker.setValue(null);
        oldWeightField.clear();
        newWeightField.clear();

        prefs.remove("name");
        prefs.remove("oldWeight");

        if (openCalendarController != null) {
            openCalendarController.refresh();
        }
    }

    @FXML
    private void onSave() {
        String name = nameField.getText();
        LocalDate date = datePicker.getValue();

        if (name == null || name.isBlank() || date == null || newWeightField.getText().isBlank()) {
            showError("Bitte Name, Datum und Neues Gewicht ausfüllen.");
            return;
        }

        double newWeight;
        try {
            newWeight = Double.parseDouble(newWeightField.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            showError("Neues Gewicht muss eine Zahl sein.");
            return;
        }

        User user = userService.findUserByName(name);
        if (user == null) {
            user = userService.createUser(name, null, 0, null);
        }

        // beim allerersten Eintrag wird das neue Gewicht auch als Altes Gewicht übernommen
        if (oldWeightField.getText().isBlank()) {
            oldWeightField.setText(newWeightField.getText());
        }

        bodyMeasurementService.createBodyMeasurement(date, newWeight, 0, 0, user);
    }

    private void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }
}

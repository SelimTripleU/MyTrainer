package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.example.entity.CalendarEntry;
import org.example.entity.User;
import org.example.service.CalendarEntryService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DayController {

    @FXML
    private Label dateLabel;

    @FXML
    private ToggleButton yesButton;

    @FXML
    private ToggleButton noButton;

    @FXML
    private ChoiceBox<String> muscleGroupChoiceBox;

    @FXML
    private TextField caloriesField;

    @FXML
    private TextField fatField;

    @FXML
    private TextField carbohydratesField;

    private final CalendarEntryService calendarEntryService = new CalendarEntryService();

    private User user;
    private LocalDate date;

    @FXML
    private void initialize() {
        ToggleGroup trainedGroup = new ToggleGroup();
        yesButton.setToggleGroup(trainedGroup);
        noButton.setToggleGroup(trainedGroup);

        muscleGroupChoiceBox.getItems().addAll(
                "Brust", "Rücken", "Beine", "Schultern", "Arme", "Bauch", "Ganzkörper");

        caloriesField.setTextFormatter(new TextFormatter<>(this::wholeNumbersOnly));
        fatField.setTextFormatter(new TextFormatter<>(this::decimalNumberOnly));
        carbohydratesField.setTextFormatter(new TextFormatter<>(this::decimalNumberOnly));
    }

    // called by CalendarController after loading, to pass the day and any existing data
    public void init(User user, LocalDate date, CalendarEntry existingEntry) {
        this.user = user;
        this.date = date;

        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMAN);
        dateLabel.setText(dayOfWeek + ", " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        if (existingEntry != null) {
            if (existingEntry.isTrained()) {
                yesButton.setSelected(true);
            } else {
                noButton.setSelected(true);
            }
            muscleGroupChoiceBox.setValue(existingEntry.getMuscleGroup());
            caloriesField.setText(String.valueOf(existingEntry.getCalories()));
            fatField.setText(String.valueOf(existingEntry.getFat()).replace('.', ','));
            carbohydratesField.setText(String.valueOf(existingEntry.getCarbohydrates()).replace('.', ','));
        }
    }

    private TextFormatter.Change wholeNumbersOnly(TextFormatter.Change change) {
        if (!change.getControlNewText().matches("\\d{0,5}")) {
            return null;
        }
        return change;
    }

    private TextFormatter.Change decimalNumberOnly(TextFormatter.Change change) {
        if (!change.getControlNewText().matches("\\d{0,4}(,\\d{0,2})?")) {
            return null;
        }
        return change;
    }

    @FXML
    private void onSave() {
        if (!yesButton.isSelected() && !noButton.isSelected()) {
            showError("Bitte angeben, ob du trainiert hast.");
            return;
        }

        boolean trained = yesButton.isSelected();

        if (trained && muscleGroupChoiceBox.getValue() == null) {
            showError("Bitte eine Muskelgruppe auswählen.");
            return;
        }

        int calories = caloriesField.getText().isBlank() ? 0 : Integer.parseInt(caloriesField.getText());
        double fat = commaTextToDouble(fatField.getText());
        double carbohydrates = commaTextToDouble(carbohydratesField.getText());
        String muscleGroup = trained ? muscleGroupChoiceBox.getValue() : null;

        calendarEntryService.saveEntry(date, trained, muscleGroup, calories, fat, carbohydrates, user);

        ((Stage) dateLabel.getScene().getWindow()).close();
    }

    private double commaTextToDouble(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return Double.parseDouble(text.replace(',', '.'));
    }

    private void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }
}

package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.example.entity.Exercise;
import org.example.entity.User;
import org.example.entity.Workout;
import org.example.entity.WorkoutExercise;
import org.example.service.ExerciseService;
import org.example.service.WorkoutExerciseService;
import org.example.service.WorkoutService;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class UebungenController {

    @FXML
    private ChoiceBox<String> muskelgruppeChoiceBox;

    @FXML
    private ListView<String> uebungenListView;

    @FXML
    private TextField setsField;

    @FXML
    private TextField wiederholungenField;

    @FXML
    private TextField gewichtField;

    @FXML
    private TextField pauseField;

    @FXML
    private ListView<String> heuteListView;

    private final ExerciseService exerciseService = new ExerciseService();
    private final WorkoutService workoutService = new WorkoutService();
    private final WorkoutExerciseService workoutExerciseService = new WorkoutExerciseService();

    private User user;
    private List<Exercise> aktuelleUebungen;
    private List<WorkoutExercise> heutigeWorkoutExercises;

    // Vorschlag für Sets/Wiederholungen/Gewicht/Pause, sobald eine Übung ausgewählt wird
    private static final int STANDARD_SETS = 3;
    private static final int STANDARD_WIEDERHOLUNGEN = 10;
    private static final String STANDARD_GEWICHT = "0";
    private static final int STANDARD_PAUSE = 60;

    @FXML
    private void initialize() {
        muskelgruppeChoiceBox.getItems().addAll(
                "Brust", "Rücken", "Beine", "Schultern", "Arme", "Bauch", "Ganzkörper");
        muskelgruppeChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, alt, neu) -> aktualisiereUebungsliste());

        uebungenListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, alt, neu) -> {
                    if (neu != null) {
                        setzeStandardwerte();
                    }
                });

        setsField.setTextFormatter(new TextFormatter<>(this::nurGanzeZahlen));
        wiederholungenField.setTextFormatter(new TextFormatter<>(this::nurGanzeZahlen));
        pauseField.setTextFormatter(new TextFormatter<>(this::nurGanzeZahlen));
        gewichtField.setTextFormatter(new TextFormatter<>(this::nurGewicht));
    }

    private void setzeStandardwerte() {
        setsField.setText(String.valueOf(STANDARD_SETS));
        wiederholungenField.setText(String.valueOf(STANDARD_WIEDERHOLUNGEN));
        gewichtField.setText(STANDARD_GEWICHT);
        pauseField.setText(String.valueOf(STANDARD_PAUSE));
    }

    public void init(User user) {
        this.user = user;
        exerciseService.seedStandardUebungenFallsLeer();
        muskelgruppeChoiceBox.setValue(muskelgruppeChoiceBox.getItems().get(0));
        aktualisiereHeutigesTraining();
    }

    private TextFormatter.Change nurGanzeZahlen(TextFormatter.Change change) {
        if (!change.getControlNewText().matches("\\d{0,3}")) {
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

    private void aktualisiereUebungsliste() {
        String muskelgruppe = muskelgruppeChoiceBox.getValue();
        if (muskelgruppe == null) {
            return;
        }

        aktuelleUebungen = exerciseService.findExercisesByMuscleGroup(muskelgruppe);

        uebungenListView.getItems().clear();
        for (Exercise exercise : aktuelleUebungen) {
            uebungenListView.getItems().add(exercise.getName());
        }
    }

    private void aktualisiereHeutigesTraining() {
        heuteListView.getItems().clear();

        Workout heutigesWorkout = workoutService.findWorkoutByUserAndDatum(user, LocalDate.now());
        if (heutigesWorkout == null) {
            heutigeWorkoutExercises = List.of();
            return;
        }

        heutigeWorkoutExercises = workoutExerciseService.findWorkoutExercisesByWorkout(heutigesWorkout);

        for (WorkoutExercise workoutExercise : heutigeWorkoutExercises) {
            heuteListView.getItems().add(String.format(Locale.GERMANY,
                    "%s – %dx%d @ %.1f kg, %ds Pause",
                    workoutExercise.getExercise().getName(),
                    workoutExercise.getSets(),
                    workoutExercise.getRepetitions(),
                    workoutExercise.getWeight(),
                    workoutExercise.getResttime()));
        }
    }

    @FXML
    private void onHinzufuegen() {
        int ausgewaehlterIndex = uebungenListView.getSelectionModel().getSelectedIndex();
        if (ausgewaehlterIndex < 0) {
            zeigeFehler("Bitte eine Übung auswählen.");
            return;
        }

        if (setsField.getText().isBlank() || wiederholungenField.getText().isBlank()
                || gewichtField.getText().isBlank() || pauseField.getText().isBlank()) {
            zeigeFehler("Bitte Sets, Wiederholungen, Gewicht und Pause ausfüllen.");
            return;
        }

        int sets = Integer.parseInt(setsField.getText());
        int wiederholungen = Integer.parseInt(wiederholungenField.getText());
        double gewicht = Double.parseDouble(gewichtField.getText().replace(',', '.'));
        int pause = Integer.parseInt(pauseField.getText());

        Exercise exercise = aktuelleUebungen.get(ausgewaehlterIndex);

        Workout workout = workoutService.findWorkoutByUserAndDatum(user, LocalDate.now());
        if (workout == null) {
            workout = workoutService.createWorkout(LocalDate.now(), null, null, 0, null, user);
        }

        workoutExerciseService.createWorkoutExercise(sets, wiederholungen, gewicht, pause, workout, exercise);

        setsField.clear();
        wiederholungenField.clear();
        gewichtField.clear();
        pauseField.clear();

        aktualisiereHeutigesTraining();
    }

    @FXML
    private void onLoeschen() {
        int ausgewaehlterIndex = heuteListView.getSelectionModel().getSelectedIndex();
        if (ausgewaehlterIndex < 0) {
            zeigeFehler("Bitte eine Übung aus dem heutigen Training auswählen.");
            return;
        }

        WorkoutExercise workoutExercise = heutigeWorkoutExercises.get(ausgewaehlterIndex);
        workoutExerciseService.deleteWorkoutExercise(workoutExercise.getId());

        aktualisiereHeutigesTraining();
    }

    private void zeigeFehler(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }
}

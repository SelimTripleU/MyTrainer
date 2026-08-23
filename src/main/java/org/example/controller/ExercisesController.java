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

public class ExercisesController {

    @FXML
    private ChoiceBox<String> muscleGroupChoiceBox;

    @FXML
    private ListView<String> exercisesListView;

    @FXML
    private TextField setsField;

    @FXML
    private TextField repetitionsField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField restTimeField;

    @FXML
    private ListView<String> todayListView;

    private final ExerciseService exerciseService = new ExerciseService();
    private final WorkoutService workoutService = new WorkoutService();
    private final WorkoutExerciseService workoutExerciseService = new WorkoutExerciseService();

    private User user;
    private List<Exercise> currentExercises;
    private List<WorkoutExercise> todaysWorkoutExercises;

    // suggested Sets/Repetitions/Weight/Rest as soon as an exercise is selected
    private static final int DEFAULT_SETS = 3;
    private static final int DEFAULT_REPETITIONS = 10;
    private static final String DEFAULT_WEIGHT = "0";
    private static final int DEFAULT_REST_TIME = 60;

    @FXML
    private void initialize() {
        muscleGroupChoiceBox.getItems().addAll(
                "Brust", "Rücken", "Beine", "Schultern", "Arme", "Bauch", "Ganzkörper");
        muscleGroupChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, updated) -> refreshExerciseList());

        exercisesListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, updated) -> {
                    if (updated != null) {
                        applyDefaultValues();
                    }
                });

        setsField.setTextFormatter(new TextFormatter<>(this::wholeNumbersOnly));
        repetitionsField.setTextFormatter(new TextFormatter<>(this::wholeNumbersOnly));
        restTimeField.setTextFormatter(new TextFormatter<>(this::wholeNumbersOnly));
        weightField.setTextFormatter(new TextFormatter<>(this::weightOnly));
    }

    private void applyDefaultValues() {
        setsField.setText(String.valueOf(DEFAULT_SETS));
        repetitionsField.setText(String.valueOf(DEFAULT_REPETITIONS));
        weightField.setText(DEFAULT_WEIGHT);
        restTimeField.setText(String.valueOf(DEFAULT_REST_TIME));
    }

    public void init(User user) {
        this.user = user;
        exerciseService.seedStandardExercisesIfEmpty();
        muscleGroupChoiceBox.setValue(muscleGroupChoiceBox.getItems().get(0));
        refreshTodaysTraining();
    }

    private TextFormatter.Change wholeNumbersOnly(TextFormatter.Change change) {
        if (!change.getControlNewText().matches("\\d{0,3}")) {
            return null;
        }
        return change;
    }

    private TextFormatter.Change weightOnly(TextFormatter.Change change) {
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

    private void refreshExerciseList() {
        String muscleGroup = muscleGroupChoiceBox.getValue();
        if (muscleGroup == null) {
            return;
        }

        currentExercises = exerciseService.findExercisesByMuscleGroup(muscleGroup);

        exercisesListView.getItems().clear();
        for (Exercise exercise : currentExercises) {
            exercisesListView.getItems().add(exercise.getName());
        }
    }

    private void refreshTodaysTraining() {
        todayListView.getItems().clear();

        Workout todaysWorkout = workoutService.findWorkoutByUserAndDate(user, LocalDate.now());
        if (todaysWorkout == null) {
            todaysWorkoutExercises = List.of();
            return;
        }

        todaysWorkoutExercises = workoutExerciseService.findWorkoutExercisesByWorkout(todaysWorkout);

        for (WorkoutExercise workoutExercise : todaysWorkoutExercises) {
            todayListView.getItems().add(String.format(Locale.GERMANY,
                    "%s – %dx%d @ %.1f kg, %ds Pause",
                    workoutExercise.getExercise().getName(),
                    workoutExercise.getSets(),
                    workoutExercise.getRepetitions(),
                    workoutExercise.getWeight(),
                    workoutExercise.getResttime()));
        }
    }

    @FXML
    private void onAdd() {
        int selectedIndex = exercisesListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Bitte eine Übung auswählen.");
            return;
        }

        if (setsField.getText().isBlank() || repetitionsField.getText().isBlank()
                || weightField.getText().isBlank() || restTimeField.getText().isBlank()) {
            showError("Bitte Sets, Wiederholungen, Gewicht und Pause ausfüllen.");
            return;
        }

        int sets = Integer.parseInt(setsField.getText());
        int repetitions = Integer.parseInt(repetitionsField.getText());
        double weight = Double.parseDouble(weightField.getText().replace(',', '.'));
        int restTime = Integer.parseInt(restTimeField.getText());

        Exercise exercise = currentExercises.get(selectedIndex);

        Workout workout = workoutService.findWorkoutByUserAndDate(user, LocalDate.now());
        if (workout == null) {
            workout = workoutService.createWorkout(LocalDate.now(), null, null, 0, null, user);
        }

        workoutExerciseService.createWorkoutExercise(sets, repetitions, weight, restTime, workout, exercise);

        setsField.clear();
        repetitionsField.clear();
        weightField.clear();
        restTimeField.clear();

        refreshTodaysTraining();
    }

    @FXML
    private void onDelete() {
        int selectedIndex = todayListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Bitte eine Übung aus dem heutigen Training auswählen.");
            return;
        }

        WorkoutExercise workoutExercise = todaysWorkoutExercises.get(selectedIndex);
        workoutExerciseService.deleteWorkoutExercise(workoutExercise.getId());

        refreshTodaysTraining();
    }

    private void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }
}

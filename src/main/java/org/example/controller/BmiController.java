package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.example.entity.User;
import org.example.service.NutritionGoalService;

import java.util.Locale;

public class BmiController {

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private TextField ageField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField weightField;

    @FXML
    private ChoiceBox<String> activityChoiceBox;

    @FXML
    private ChoiceBox<String> goalChoiceBox;

    @FXML
    private Label bmiLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label caloriesLabel;

    @FXML
    private Label proteinLabel;

    @FXML
    private Label fatLabel;

    @FXML
    private Label carbohydratesLabel;

    private final NutritionGoalService nutritionGoalService = new NutritionGoalService();

    private User user;

    // activity factors on the PAL scale, used to scale the basal metabolic rate up to total calorie needs
    private static final double[] ACTIVITY_FACTORS = {1.2, 1.375, 1.55, 1.725, 1.9};

    // typical calorie deficit/surplus for a moderate, healthy weight change
    private static final double[] GOAL_CALORIE_ADJUSTMENT = {-500, 0, 500};

    public void init(User user) {
        this.user = user;
    }

    @FXML
    private void initialize() {
        genderChoiceBox.getItems().addAll("Männlich", "Weiblich");
        genderChoiceBox.setValue("Männlich");

        activityChoiceBox.getItems().addAll(
                "Sitzend (kaum Bewegung)",
                "Leicht aktiv (1-3x Sport/Woche)",
                "Mäßig aktiv (3-5x Sport/Woche)",
                "Aktiv (6-7x Sport/Woche)",
                "Sehr aktiv (körperliche Arbeit/Leistungssport)");
        activityChoiceBox.setValue("Leicht aktiv (1-3x Sport/Woche)");

        goalChoiceBox.getItems().addAll("Abnehmen", "Gewicht halten", "Zunehmen");
        goalChoiceBox.setValue("Gewicht halten");

        // age and height: whole numbers only, so typos aren't possible
        ageField.setTextFormatter(new TextFormatter<>(this::wholeNumbersOnly));
        heightField.setTextFormatter(new TextFormatter<>(this::wholeNumbersOnly));

        // weight: digits and one comma only, at most 4 digits total (e.g. 88,24)
        weightField.setTextFormatter(new TextFormatter<>(this::weightOnly));
    }

    private TextFormatter.Change wholeNumbersOnly(TextFormatter.Change change) {
        String newText = change.getControlNewText();

        if (!newText.matches("\\d{0,3}")) {
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

    @FXML
    private void onCalculate() {
        if (ageField.getText().isBlank() || heightField.getText().isBlank() || weightField.getText().isBlank()) {
            showError("Bitte Alter, Größe und Gewicht ausfüllen.");
            return;
        }

        int age = Integer.parseInt(ageField.getText());
        int heightCm = Integer.parseInt(heightField.getText());
        double weightKg = Double.parseDouble(weightField.getText().replace(',', '.'));

        if (age <= 0 || heightCm <= 0 || weightKg <= 0) {
            showError("Bitte gültige Werte größer als 0 eingeben.");
            return;
        }

        double heightM = heightCm / 100.0;
        double bmi = weightKg / (heightM * heightM);

        bmiLabel.setText(String.format(Locale.GERMANY, "Dein BMI: %.1f", bmi));
        categoryLabel.setText("Kategorie: " + bmiCategory(bmi));

        boolean isMale = genderChoiceBox.getValue().equals("Männlich");
        double basalMetabolicRate = isMale
                ? 10 * weightKg + 6.25 * heightCm - 5 * age + 5
                : 10 * weightKg + 6.25 * heightCm - 5 * age - 161;

        double activityFactor = ACTIVITY_FACTORS[activityChoiceBox.getSelectionModel().getSelectedIndex()];
        double maintenanceCalories = basalMetabolicRate * activityFactor;

        double goalAdjustment = GOAL_CALORIE_ADJUSTMENT[goalChoiceBox.getSelectionModel().getSelectedIndex()];
        double calorieTarget = maintenanceCalories + goalAdjustment;

        // nutrient split: 1.8g protein per kg body weight, 25% of calories as fat, rest carbohydrates
        double proteinG = weightKg * 1.8;
        double proteinKcal = proteinG * 4;

        double fatKcal = calorieTarget * 0.25;
        double fatG = fatKcal / 9;

        double carbohydrateKcal = calorieTarget - proteinKcal - fatKcal;
        double carbohydrateG = carbohydrateKcal / 4;

        caloriesLabel.setText(String.format(Locale.GERMANY, "Kalorienziel: %.0f kcal/Tag", calorieTarget));
        proteinLabel.setText(String.format(Locale.GERMANY, "Protein: %.0f g/Tag", proteinG));
        fatLabel.setText(String.format(Locale.GERMANY, "Fett: %.0f g/Tag", fatG));
        carbohydratesLabel.setText(String.format(Locale.GERMANY, "Kohlenhydrate: %.0f g/Tag", carbohydrateG));

        // so the Food window can remind the user how much they're allowed to eat each day
        nutritionGoalService.saveGoal((int) Math.round(calorieTarget), proteinG, fatG, carbohydrateG,
                goalChoiceBox.getValue(), user);
    }

    private String bmiCategory(double bmi) {
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

    private void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }
}

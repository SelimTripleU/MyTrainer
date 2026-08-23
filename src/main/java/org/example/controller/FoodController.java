package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;
import org.example.entity.NutritionGoal;
import org.example.entity.Recipe;
import org.example.entity.User;
import org.example.service.NutritionGoalService;
import org.example.service.RecipeService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FoodController {

    @FXML
    private Label caloriesLabel;

    @FXML
    private Label proteinLabel;

    @FXML
    private Label fatLabel;

    @FXML
    private Label carbohydratesLabel;

    @FXML
    private ListView<String> recipesListView;

    private final RecipeService recipeService = new RecipeService();
    private final NutritionGoalService nutritionGoalService = new NutritionGoalService();

    private List<Recipe> currentRecipes;

    @FXML
    private void initialize() {
        recipeService.seedStandardRecipesIfEmpty();

        recipesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showRecipe();
            }
        });
    }

    public void init(User user) {
        NutritionGoal goal = nutritionGoalService.findGoalForUser(user);

        if (goal == null) {
            caloriesLabel.setText("Noch keine Werte berechnet.");
            proteinLabel.setText("Bitte zuerst im BMI-Rechner deine Werte berechnen.");
            fatLabel.setText("");
            carbohydratesLabel.setText("");

            currentRecipes = recipeService.findAllRecipes();
        } else {
            caloriesLabel.setText(String.format(Locale.GERMANY, "Kalorienziel: %d kcal/Tag", goal.getCalories()));
            proteinLabel.setText(String.format(Locale.GERMANY, "Protein: %.0f g/Tag", goal.getProtein()));
            fatLabel.setText(String.format(Locale.GERMANY, "Fett: %.0f g/Tag", goal.getFat()));
            carbohydratesLabel.setText(String.format(Locale.GERMANY, "Kohlenhydrate: %.0f g/Tag", goal.getCarbohydrates()));

            currentRecipes = "Gewicht halten".equals(goal.getGoal())
                    ? recipeService.findAllRecipes()
                    : recipeService.findRecipesByGoal(goal.getGoal());
        }

        recipesListView.getItems().clear();
        for (Recipe recipe : currentRecipes) {
            recipesListView.getItems().add(recipe.getName());
        }
    }

    private void showRecipe() {
        int selectedIndex = recipesListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            return;
        }

        Recipe recipe = currentRecipes.get(selectedIndex);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("rezeptDetail.fxml"));
            Parent root = fxmlLoader.load();

            RecipeDetailController recipeDetailController = fxmlLoader.getController();
            recipeDetailController.init(recipe);

            Stage recipeStage = new Stage();
            recipeStage.setTitle(recipe.getName());
            recipeStage.initModality(Modality.APPLICATION_MODAL);
            recipeStage.setScene(new Scene(root));
            recipeStage.show();
        } catch (IOException e) {
            showError("Rezept konnte nicht geöffnet werden.");
        }
    }

    private void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.showAndWait();
    }
}

package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.entity.Recipe;

import java.util.Locale;

public class RecipeDetailController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label nutritionLabel;

    @FXML
    private Label recipeTextLabel;

    public void init(Recipe recipe) {
        nameLabel.setText(recipe.getName());
        nutritionLabel.setText(String.format(Locale.GERMANY,
                "%d kcal – %.0fg Protein, %.0fg Fett, %.0fg Kohlenhydrate",
                recipe.getCalories(), recipe.getProtein(), recipe.getFat(), recipe.getCarbohydrates()));
        recipeTextLabel.setText(recipe.getRecipeText());
    }
}

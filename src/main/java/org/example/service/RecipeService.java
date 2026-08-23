package org.example.service;

import org.example.dao.RecipeRepository;
import org.example.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeService {

    private final RecipeRepository recipeRepository = new RecipeRepository();

    public Recipe createRecipe(String name, int calories, double protein, double fat, double carbohydrates,
                                String goal, String recipeText) {

        Recipe recipe = new Recipe();

        recipe.setName(name);
        recipe.setCalories(calories);
        recipe.setProtein(protein);
        recipe.setFat(fat);
        recipe.setCarbohydrates(carbohydrates);
        recipe.setGoal(goal);
        recipe.setRecipeText(recipeText);

        recipeRepository.save(recipe);

        return recipe;
    }

    public List<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> findRecipesByGoal(String goal) {
        List<Recipe> result = new ArrayList<>();

        for (Recipe recipe : findAllRecipes()) {
            if (recipe.getGoal().equals(goal)) {
                result.add(recipe);
            }
        }

        return result;
    }

    // legt beim allerersten Start ein paar Beispielrezepte für Abnehmen und Zunehmen an
    public void seedStandardRecipesIfEmpty() {
        if (!findAllRecipes().isEmpty()) {
            return;
        }

        createRecipe("Gegrilltes Hähnchen mit Gemüse", 350, 40, 8, 20, "Abnehmen",
                "200g Hähnchenbrust würzen und ohne Öl in der Pfanne anbraten. Dazu 300g gedünstetes Gemüse "
                        + "(Brokkoli, Paprika, Zucchini) servieren.");
        createRecipe("Magerquark mit Beeren", 220, 25, 2, 20, "Abnehmen",
                "250g Magerquark mit 100g gemischten Beeren und einem Teelöffel Honig verrühren.");
        createRecipe("Gemüsesuppe mit Linsen", 280, 18, 3, 40, "Abnehmen",
                "1 Zwiebel und 2 Karotten anschwitzen, 100g rote Linsen und 500ml Gemüsebrühe dazugeben "
                        + "und 20 Minuten köcheln lassen.");
        createRecipe("Lachsfilet mit Salat", 400, 35, 20, 10, "Abnehmen",
                "150g Lachsfilet in der Pfanne braten, dazu einen großen gemischten Salat mit "
                        + "Essig-Öl-Dressing servieren.");
        createRecipe("Omelett mit Spinat und Feta", 300, 24, 20, 5, "Abnehmen",
                "3 Eier verquirlen und mit 100g Spinat und 30g Feta in der Pfanne stocken lassen.");
        createRecipe("Putenbrust mit Süßkartoffel", 380, 38, 6, 40, "Abnehmen",
                "150g Putenbrust würzen und braten, dazu 200g Süßkartoffel im Ofen backen.");
        createRecipe("Griechischer Joghurt mit Nüssen", 250, 20, 12, 15, "Abnehmen",
                "200g griechischen Joghurt mit einer Handvoll Walnüssen und etwas Zimt mischen.");
        createRecipe("Zucchini-Nudeln mit Garnelen", 320, 32, 10, 15, "Abnehmen",
                "2 Zucchini spiralisieren, 200g Garnelen in Knoblauch und Olivenöl anbraten und mit den "
                        + "Zucchini-Nudeln vermengen.");
        createRecipe("Hähnchen-Gemüse-Pfanne", 360, 38, 10, 25, "Abnehmen",
                "200g Hähnchenbrust würfeln und mit buntem Gemüse (Paprika, Champignons, Zwiebeln) in der "
                        + "Pfanne braten, mit Sojasoße abschmecken.");
        createRecipe("Eiweißbrot mit Frischkäse und Gurke", 280, 22, 8, 25, "Abnehmen",
                "2 Scheiben Eiweißbrot mit fettarmem Frischkäse bestreichen und mit Gurkenscheiben belegen.");
        createRecipe("Thunfischsalat", 300, 30, 12, 10, "Abnehmen",
                "1 Dose Thunfisch im eigenen Saft mit Salat, Tomaten, Gurken und einem Löffel fettarmem "
                        + "Joghurt mischen.");
        createRecipe("Gebackener Fisch mit Ofengemüse", 380, 35, 12, 30, "Abnehmen",
                "200g weißer Fisch (z.B. Kabeljau) würzen und mit Ofengemüse (Karotten, Zucchini, Paprika) "
                        + "bei 200°C 20 Minuten backen.");

        createRecipe("Nudeln mit Hackfleischsoße", 750, 40, 25, 80, "Zunehmen",
                "300g Nudeln kochen. 250g Rinderhack mit Zwiebel anbraten, Tomatensoße dazugeben und "
                        + "mit den Nudeln und etwas Parmesan servieren.");
        createRecipe("Haferflocken-Porridge mit Nussmus und Banane", 650, 20, 22, 90, "Zunehmen",
                "100g Haferflocken mit 300ml Milch aufkochen, mit 1 Banane, 2 EL Erdnussmus und Honig "
                        + "servieren.");
        createRecipe("Reis mit Hähnchen und Avocado", 700, 45, 25, 70, "Zunehmen",
                "150g Reis kochen, 200g Hähnchenbrust braten, mit einer halben Avocado und Sojasoße "
                        + "servieren.");
        createRecipe("Proteinshake mit Erdnussbutter", 600, 45, 20, 60, "Zunehmen",
                "500ml Milch, 1 Portion Proteinpulver, 1 Banane und 2 EL Erdnussbutter im Mixer pürieren.");
        createRecipe("Vollkornbrot mit Erdnussbutter und Banane", 620, 20, 25, 75, "Zunehmen",
                "3 Scheiben Vollkornbrot reichlich mit Erdnussbutter bestreichen und mit Bananenscheiben "
                        + "belegen.");
        createRecipe("Rindersteak mit Ofenkartoffeln", 780, 50, 35, 50, "Zunehmen",
                "250g Rindersteak scharf anbraten, dazu 300g Ofenkartoffeln mit Sauerrahm servieren.");
        createRecipe("Linsen-Dal mit Reis", 680, 28, 15, 95, "Zunehmen",
                "200g rote Linsen mit Kokosmilch, Curry und Zwiebeln köcheln lassen, dazu 150g "
                        + "Basmatireis servieren.");
        createRecipe("Lachs mit Süßkartoffelpüree", 720, 42, 30, 60, "Zunehmen",
                "200g Lachsfilet braten, 300g Süßkartoffeln kochen und mit Butter zu Püree stampfen.");
        createRecipe("Falafel-Wrap mit Hummus", 700, 22, 28, 85, "Zunehmen",
                "Einen großen Wrap mit Hummus bestreichen, mit 6 Falafelbällchen, Salat und Joghurtsoße "
                        + "füllen.");
        createRecipe("Rührei mit Speck und Toast", 680, 35, 40, 40, "Zunehmen",
                "4 Eier mit 50g Speck in der Pfanne braten, mit 3 Scheiben getoastetem Vollkornbrot "
                        + "servieren.");
        createRecipe("Chili con Carne mit Reis", 800, 45, 25, 90, "Zunehmen",
                "250g Rinderhack mit Kidneybohnen, Mais und Tomaten köcheln lassen, mit 150g Reis "
                        + "servieren.");
        createRecipe("Milchreis mit Nüssen und Honig", 650, 18, 20, 95, "Zunehmen",
                "150g Milchreis mit Vollmilch kochen, mit einer Handvoll gehackten Nüssen und Honig "
                        + "verfeinern.");
    }

}

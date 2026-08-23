package org.example.dao;

import org.example.entity.Recipe;

public class RecipeRepository extends GenericDao<Recipe> {

    public RecipeRepository() {
        super(Recipe.class);
    }
}

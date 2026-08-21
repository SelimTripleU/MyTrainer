package org.example.dao;

import org.example.entity.Meal;

public class MealRepository extends GenericDao<Meal> {

    public MealRepository() {
        super(Meal.class);
    }
}

package org.example.dao;

import org.example.entity.NutritionGoal;

public class NutritionGoalRepository extends GenericDao<NutritionGoal> {

    public NutritionGoalRepository() {
        super(NutritionGoal.class);
    }
}

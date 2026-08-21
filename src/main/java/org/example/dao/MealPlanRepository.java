package org.example.dao;

import org.example.entity.MealPlan;

public class MealPlanRepository extends GenericDao<MealPlan> {

    public MealPlanRepository() {
        super(MealPlan.class);
    }
}

package org.example.service;

import org.example.dao.MealPlanRepository;
import org.example.entity.MealPlan;
import org.example.entity.User;

import java.time.LocalDate;

public class MealPlanService {

    private final MealPlanRepository mealPlanRepository = new MealPlanRepository();

    public MealPlan createMealPlan(LocalDate date, int targetCalories, User user) {

        MealPlan mealPlan = new MealPlan();

        mealPlan.setDate(date);
        mealPlan.setTargetCalories(targetCalories);
        mealPlan.setUser(user);

        mealPlanRepository.save(mealPlan);

        return mealPlan;
    }

    public void deleteMealPlan(int i) {
        MealPlan mealPlanToBeDeleted = mealPlanRepository.findById(Long.valueOf(i));

        mealPlanRepository.delete(mealPlanToBeDeleted);

    }

    public void modifyMealPlan(MealPlan modifiedMealPlan) {
        mealPlanRepository.update(modifiedMealPlan);
    }

    public MealPlan findMealPlanById(int id) {
        return mealPlanRepository.findById(Long.valueOf(id));
    }

}
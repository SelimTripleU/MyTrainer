package org.example.service;

import org.example.dao.MealRepository;
import org.example.entity.Meal;
import org.example.entity.MealPlan;

public class MealService {

    private final MealRepository mealRepository = new MealRepository();

    public Meal createMeal(String name, int calories, double protein, double carbohydrates, double fat, MealPlan mealPlan) {

        Meal meal = new Meal();

        meal.setName(name);
        meal.setCalories(calories);
        meal.setProtein(protein);
        meal.setCarbohydrates(carbohydrates);
        meal.setFat(fat);
        meal.setMealPlan(mealPlan);

        mealRepository.save(meal);

        return meal;
    }

    public void deleteMeal(int i) {
        Meal mealToBeDeleted = mealRepository.findById(Long.valueOf(i));

        mealRepository.delete(mealToBeDeleted);

    }

    public void modifyMeal(Meal modifiedMeal) {
        mealRepository.update(modifiedMeal);
    }

    public Meal findMealById(int id) {
        return mealRepository.findById(Long.valueOf(id));
    }

}
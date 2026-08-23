package org.example.service;

import org.example.dao.NutritionGoalRepository;
import org.example.entity.NutritionGoal;
import org.example.entity.User;

public class NutritionGoalService {

    private final NutritionGoalRepository nutritionGoalRepository = new NutritionGoalRepository();

    // saves the goal last calculated in the BMI calculator for the user, overwriting an existing one
    public NutritionGoal saveGoal(int calories, double protein, double fat, double carbohydrates,
                                   String goal, User user) {
        NutritionGoal nutritionGoal = findGoalForUser(user);

        if (nutritionGoal == null) {
            nutritionGoal = new NutritionGoal();
            nutritionGoal.setUser(user);
        }

        nutritionGoal.setCalories(calories);
        nutritionGoal.setProtein(protein);
        nutritionGoal.setFat(fat);
        nutritionGoal.setCarbohydrates(carbohydrates);
        nutritionGoal.setGoal(goal);

        if (nutritionGoal.getId() == 0) {
            nutritionGoalRepository.save(nutritionGoal);
        } else {
            nutritionGoalRepository.update(nutritionGoal);
        }

        return nutritionGoal;
    }

    public NutritionGoal findGoalForUser(User user) {
        for (NutritionGoal nutritionGoal : nutritionGoalRepository.findAll()) {
            if (nutritionGoal.getUser().getId() == user.getId()) {
                return nutritionGoal;
            }
        }

        return null;
    }

}

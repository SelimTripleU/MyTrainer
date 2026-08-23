package org.example;

import org.example.entity.BodyMeasurement;
import org.example.entity.Exercise;
import org.example.entity.Meal;
import org.example.entity.MealPlan;
import org.example.entity.User;
import org.example.entity.Workout;
import org.example.entity.WorkoutExercise;
import org.example.service.BodyMeasurementService;
import org.example.service.ExerciseService;
import org.example.service.MealPlanService;
import org.example.service.MealService;
import org.example.service.UserService;
import org.example.service.WorkoutExerciseService;
import org.example.service.WorkoutService;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestBackend {
    public static void main(String[] args) {
        UserService userService = new UserService();
        BodyMeasurementService bodyMeasurementService = new BodyMeasurementService();
        ExerciseService exerciseService = new ExerciseService();
        WorkoutService workoutService = new WorkoutService();
        WorkoutExerciseService workoutExerciseService = new WorkoutExerciseService();
        MealPlanService mealPlanService = new MealPlanService();
        MealService mealService = new MealService();

        // create user
        User user = userService.createUser(
                "Kevin",
                LocalDate.of(2002, 10, 22),
                1.90,
                "male"
        );

        // check that the user was saved
        System.out.println("User ID: " + user.getId());

        // create BodyMeasurement for the user
        BodyMeasurement measurement = bodyMeasurementService.createBodyMeasurement(
                LocalDate.now(),
                85,
                1.90,
                18,
                user
        );
        System.out.println("Body Measurement fat: " + measurement.getBodyfat());
        measurement.setBodyfat(99.9);
        bodyMeasurementService.modifyBodyMeasurement(measurement);
        BodyMeasurement modifiedBodyMeasurement = bodyMeasurementService.findBodyMeasurementById(measurement.getId());
        System.out.println("New bodyfat: " + modifiedBodyMeasurement.getBodyfat());

        // create exercise
        Exercise exercise = exerciseService.createExercise(
                "Bankdrücken",
                "Brust",
                "Langhantel Bankdrücken für Brust und Trizeps"
        );
        System.out.println("Exercise ID: " + exercise.getId());
        exercise.setMuscleGroup("Brust/Trizeps");
        exerciseService.modifyExercise(exercise);
        Exercise modifiedExercise = exerciseService.findExerciseById(exercise.getId());
        System.out.println("New muscle group: " + modifiedExercise.getMuscleGroup());

        // create workout for Kevin
        Workout workout = workoutService.createWorkout(
                LocalDate.now(),
                LocalTime.of(18, 0),
                LocalTime.of(19, 15),
                75,
                "Push Day, hat sich gut angefühlt",
                user
        );
        System.out.println("Workout ID: " + workout.getId());
        workout.setNotes("Push Day, war ziemlich hart heute");
        workoutService.modifyWorkout(workout);
        Workout modifiedWorkout = workoutService.findWorkoutById(workout.getId());
        System.out.println("New notes: " + modifiedWorkout.getNotes());

        // record WorkoutExercise, i.e. Bankdrücken, in the workout
        WorkoutExercise workoutExercise = workoutExerciseService.createWorkoutExercise(
                4,
                8,
                80.0,
                90,
                workout,
                exercise
        );
        System.out.println("WorkoutExercise ID: " + workoutExercise.getId());
        workoutExercise.setWeight(82.5);
        workoutExerciseService.modifyWorkoutExercise(workoutExercise);
        WorkoutExercise modifiedWorkoutExercise = workoutExerciseService.findWorkoutExerciseById(workoutExercise.getId());
        System.out.println("New weight: " + modifiedWorkoutExercise.getWeight());

        // create MealPlan for Kevin
        MealPlan mealPlan = mealPlanService.createMealPlan(
                LocalDate.now(),
                2800,
                user
        );
        System.out.println("MealPlan ID: " + mealPlan.getId());
        mealPlan.setTargetCalories(3000);
        mealPlanService.modifyMealPlan(mealPlan);
        MealPlan modifiedMealPlan = mealPlanService.findMealPlanById(mealPlan.getId());
        System.out.println("New target calories: " + modifiedMealPlan.getTargetCalories());

        // create Meal in the MealPlan
        Meal meal = mealService.createMeal(
                "Hähnchen mit Reis",
                650,
                45.0,
                70.0,
                12.0,
                mealPlan
        );
        System.out.println("Meal ID: " + meal.getId());
        meal.setCalories(700);
        mealService.modifyMeal(meal);
        Meal modifiedMeal = mealService.findMealById(meal.getId());
        System.out.println("New calories: " + modifiedMeal.getCalories());

        // finally, quickly check that delete also works
        mealService.deleteMeal(meal.getId());
        System.out.println("Meal " + meal.getId() + " was deleted");
    }


}
